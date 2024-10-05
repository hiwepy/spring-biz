package org.springframework.biz.web.servlet.mvc.method.annotation;

import org.springframework.biz.config.Ini;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 通过表达式将请求映射到指定的方法上
 */
public class RoutableRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	protected static Field mappingRegistryField = ReflectionUtils.findField(RequestMappingHandlerMapping.class, "mappingRegistry");

	protected AntPathMatcher pathMatcher = new AntPathMatcher();
	
	/**
	 * 处理器链定义
	 */
	private Map<String, String> handlerDefinitionMap;

	static {
		mappingRegistryField.setAccessible(true);
	}

	public RoutableRequestMappingHandlerMapping() {
		handlerDefinitionMap = new LinkedHashMap<String, String>();
	}

	public Map<String, String> getHandlerDefinitionMap() {
		return handlerDefinitionMap;
	}

	public void setHandlerDefinitionMap(Map<String, String> handlerDefinitionMap) {
		this.handlerDefinitionMap = handlerDefinitionMap;
	}
	
	public void setHandlerChainDefinitions(String definitions) throws IOException {
        Ini ini = new Ini();
        ini.load(definitions);
        Ini.Section section = ini.getSection("urls");
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
        setHandlerDefinitionMap(section);
    }

	@Override
	protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
		
		// 获取lookupPath
		String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
		if (logger.isDebugEnabled()) {
			logger.debug("Looking up handler method for path " + lookupPath);
		}
		
		Iterator<Entry<String, String>>  ite = handlerDefinitionMap.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<String, String> entry = ite.next();
			// /get*/user = get/user
			if(pathMatcher.match(entry.getKey(), lookupPath)){
				
				Object mappingRegistry = ReflectionUtils.getField(mappingRegistryField, this);
				Method acquireReadLockMethod = ReflectionUtils.findMethod(mappingRegistry.getClass(), "acquireReadLock");
				acquireReadLockMethod.setAccessible(true);
				ReflectionUtils.invokeMethod(acquireReadLockMethod, mappingRegistry);

				try {
					HandlerMethod handlerMethod = lookupHandlerMethod(entry.getValue(), request);
					if (logger.isDebugEnabled()) {
						if (handlerMethod != null) {
							logger.debug("Returning handler method [" + handlerMethod + "]");
						} else {
							logger.debug("Did not find handler method for [" + entry.getValue() + "]");
						}
					}
					return (handlerMethod != null ? handlerMethod.createWithResolvedBean() : null);
				} finally {
					Method releaseReadLockMethod = ReflectionUtils.findMethod(mappingRegistry.getClass(), "releaseReadLock");
					releaseReadLockMethod.setAccessible(true);
					ReflectionUtils.invokeMethod(releaseReadLockMethod, mappingRegistry);
				}
				
			}
		}
		return super.getHandlerInternal(request);
	}

}
