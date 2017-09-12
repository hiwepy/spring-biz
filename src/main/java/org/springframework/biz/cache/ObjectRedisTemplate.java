package org.springframework.biz.cache;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.biz.utils.GenericsUtils;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;


/**
 * 
 * @className	： ObjectRedisTemplate
 * @description	： Redis实现对象序列化存储
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年9月12日 下午11:24:46
 * @version 	V1.0 
 * @param <T>
 */
public class ObjectRedisTemplate<T> extends RedisTemplate<String, T> {

	private final ConcurrentMap<Class<T>, RedisSerializer<T>> serializerMap = new ConcurrentHashMap<Class<T>, RedisSerializer<T>>();
	private final RedisSerializer<String> objectKeySerializer = new Jackson2JsonRedisSerializer<String>(String.class);
	private RedisConnectionFactory connectionFactory;
	private boolean serializerCacheEnabled = true;

	@SuppressWarnings("unchecked")
	protected void initRedisSerializer() {
		Class<T> clazz = GenericsUtils.getSuperClassGenricType(this.getClass());
		RedisSerializer<T> objectSerializer = findForClass(clazz);
		super.setValueSerializer(objectSerializer);
		super.setHashKeySerializer(objectSerializer);
		super.setHashValueSerializer(objectSerializer);
	}

	public ObjectRedisTemplate() {
		super.setKeySerializer(getObjectKeySerializer());
		super.setConnectionFactory(getConnectionFactory());
		this.initRedisSerializer();
	}

	public ObjectRedisTemplate(RedisConnectionFactory connectionFactory) {
		super.setKeySerializer(getObjectKeySerializer());
		super.setConnectionFactory(connectionFactory);
		this.initRedisSerializer();
	}

	protected RedisConnection preProcessConnection(RedisConnection connection,
			boolean existingConnection) {
		return new DefaultStringRedisConnection(connection);
	}

	/**
	 * Returns the connectionFactory.
	 * 
	 * @return Returns the connectionFactory
	 */
	public RedisConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public RedisSerializer<T> findForClass(Class<T> type) {
		if (serializerCacheEnabled) {
			// synchronized (type) removed see issue #461
			RedisSerializer<T> cached = serializerMap.get(type);
			if (cached == null) {
				cached = new Jackson2JsonRedisSerializer<T>(type);
				serializerMap.put(type, cached);
			}
			return cached;
		} else {
			return new Jackson2JsonRedisSerializer<T>(type);
		}
	}

	/**
	 * Sets the connection factory.
	 * 
	 * @param connectionFactory
	 *            The connectionFactory to set.
	 */
	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public boolean isSerializerCacheEnabled() {
		return serializerCacheEnabled;
	}

	public void setSerializerCacheEnabled(boolean serializerCacheEnabled) {
		this.serializerCacheEnabled = serializerCacheEnabled;
	}

	public RedisSerializer<String> getObjectKeySerializer() {
		return objectKeySerializer;
	}
	
	

}
