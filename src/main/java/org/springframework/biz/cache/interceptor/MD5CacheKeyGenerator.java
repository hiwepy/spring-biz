package org.springframework.biz.cache.interceptor;

import java.lang.reflect.Method;
import java.nio.charset.Charset;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.Hashing;

/**
 * http://blog.csdn.net/syani/article/details/52239967
 */
public class MD5CacheKeyGenerator extends HashingCacheKeyGenerator {  
    
	protected static Logger LOG = LoggerFactory.getLogger(MD5CacheKeyGenerator.class);
	
    @Override  
    public Object generate(Object target, Method method, Object... params) {  
        String finalKey = DigestUtils.md5Hex(buildKey(target, method, params).toString());
        long cacheKeyHash = Hashing.murmur3_128().hashString(finalKey, Charset.defaultCharset()).asLong();  
        LOG.debug("using cache key={} hashCode={}", finalKey, cacheKeyHash);  
        return finalKey.toString();  
    }
    
}  
