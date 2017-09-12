package org.springframework.biz.cache.interceptor;

import java.lang.reflect.Method;

import org.springframework.biz.cache.CacheKey;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;

public class StaticCacheKeyGenerator implements KeyGenerator {  
    
    @Override  
    public Object generate(Object target, Method method, Object... params) {  
    	CacheKey cacheKey = method.getAnnotation(CacheKey.class);
    	if(cacheKey != null){
    		return new SimpleKey(cacheKey.value());
    	}
    	return SimpleKey.EMPTY;
    }  
}  
