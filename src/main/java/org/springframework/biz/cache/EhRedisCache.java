/*
 * Copyright (c) 2018 (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.springframework.biz.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * 
 * @className ： EhRedisCache
 * @description ： 两级缓存，一级:ehcache,二级为redisCache
 * @author ： <a href="https://github.com/vindell">vindell</a>
 * @date ： 2017年7月16日 下午1:22:40
 * @version V1.0 http://blog.csdn.net/liaoyulin0609/article/details/51787020
 */
@SuppressWarnings("unchecked")
public class EhRedisCache implements Cache {

	private static final Logger LOG = LoggerFactory.getLogger(EhRedisCache.class);

	private Ehcache ehcache;

	private RedisTemplate<String, Object> redisTemplate;

	private long liveTime = 1 * 60 * 60; // 默认1h=1*60*60

	@Override
	public String getName() {
		return this.ehcache.getName();
	}

	@Override
	public Object getNativeCache() {
		return this;
	}

	@Override
	public ValueWrapper get(Object key) {
		Element element = lookup(key);
		return toValueWrapper(element);
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		Element element = lookup(key);
		if (element != null) {
			return (T) element.getObjectValue();
		} else {
			getEhcache().acquireWriteLockOnKey(key);
			try {
				element = lookup(key); // One more attempt with the write lock
				if (element != null) {
					return (T) element.getObjectValue();
				} else {
					return loadValue(key, valueLoader);
				}
			} finally {
				getEhcache().releaseWriteLockOnKey(key);
			}
		}
	}

	private <T> T loadValue(Object key, Callable<T> valueLoader) {
		T value;
		try {
			value = valueLoader.call();
		} catch (Throwable ex) {
			throw new ValueRetrievalException(key, valueLoader, ex);
		}
		put(key, value);
		return value;
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		Element element = lookup(key);
		Object value = (element != null ? element.getObjectValue() : null);
		if (value != null && type != null && !type.isInstance(value)) {
			throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
		}
		return (T) value;
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {

		Element existingElement = getEhcache().putIfAbsent(new Element(key, value));
		final String keyStr = key.toString();
		final Object valueStr = value;
		getRedisTemplate().execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] keyb = keyStr.getBytes();
				byte[] valueb = toByteArray(valueStr);
				connection.set(keyb, valueb);
				if (liveTime > 0) {
					connection.expire(keyb, liveTime);
				}
				return 1L;
			}
		}, true);
		
		return toValueWrapper(existingElement);
	}

	@Override
	public void put(Object key, Object value) {
		getEhcache().put(new Element(key, value));
		final String keyStr = key.toString();
		final Object valueStr = value;
		getRedisTemplate().execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] keyb = keyStr.getBytes();
				byte[] valueb = toByteArray(valueStr);
				connection.set(keyb, valueb);
				if (liveTime > 0) {
					connection.expire(keyb, liveTime);
				}
				return 1L;
			}
		}, true);

	}

	@Override
	public void evict(Object key) {
		getEhcache().remove(key);
		final String keyStr = key.toString();
		getRedisTemplate().execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.del(keyStr.getBytes());
			}
		}, true);
	}

	@Override
	public void clear() {
		getEhcache().removeAll();
		getRedisTemplate().execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "clear done.";
			}
		}, true);
	}

	private Element lookup(Object key) {
		Element element = getEhcache().get(key);
		LOG.info("Cache L1 (ehcache) :{}={}", key, element);
		if (element != null) {
			return element;
		}
		// TODO 这样会不会更好？访问10次EhCache 强制访问一次redis 使得数据不失效
		final String keyStr = key.toString();
		Object objectValue = getRedisTemplate().execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] key = keyStr.getBytes();
				byte[] value = connection.get(key);
				if (value == null) {
					return null;
				}
				// 每次获得，重置缓存过期时间
				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return toObject(value);
			}
		}, true);
		element = new Element(key, objectValue);
		getEhcache().put(element);// 取出来之后缓存到本地
		LOG.info("Cache L2 (redis) :{}={}", key, objectValue);
		return element;
	}

	private ValueWrapper toValueWrapper(Element element) {
		return (element != null ? new SimpleValueWrapper(element.getObjectValue()) : null);
	}

	/**
	 * 描述 : Object转byte[]. <br>
	 * 
	 * @param obj
	 * @return
	 */
	private byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}

	/**
	 * 描述 : byte[]转Object . <br>
	 * 
	 * @param bytes
	 * @return
	 */
	private Object toObject(byte[] bytes) {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return obj;
	}

	public Ehcache getEhcache() {
		return ehcache;
	}

	public void setEhcache(Ehcache ehcache) {
		this.ehcache = ehcache;
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public long getLiveTime() {
		return liveTime;
	}

	public void setLiveTime(long liveTime) {
		this.liveTime = liveTime;
	}

}
