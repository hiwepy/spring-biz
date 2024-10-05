package org.springframework.biz.web.servlet.mvc.registry;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultDynamicControllerRegistry extends DefaultDynamicBeanDefinitionRegistry
		implements DynamicControllerDefinitionRegistry, DynamicControllerRegistry {

	// RequestMappingHandlerMapping
	protected static Method detectHandlerMethodsMethod = ReflectionUtils.findMethod(RequestMappingHandlerMapping.class,
			"detectHandlerMethods", Object.class);
	protected static Method getMappingForMethodMethod = ReflectionUtils.findMethod(RequestMappingHandlerMapping.class,
			"getMappingForMethod", Method.class, Class.class);

	protected static Field mappingRegistryField = ReflectionUtils.findField(RequestMappingHandlerMapping.class,
			"mappingRegistry");

	protected static Field injectionMetadataCacheField = ReflectionUtils
			.findField(AutowiredAnnotationBeanPostProcessor.class, "injectionMetadataCache");

	protected RequestMappingHandlerMapping requestMappingHandlerMapping;
	
	static {
		detectHandlerMethodsMethod.setAccessible(true);
		getMappingForMethodMethod.setAccessible(true);
		// urlMapField.setAccessible(true);
		mappingRegistryField.setAccessible(true);
		injectionMetadataCacheField.setAccessible(true);
	}

	public DefaultDynamicControllerRegistry() {
	}
	

	@Override
	public void registerController(Class<?> controllerClass) {
		this.registerController(controllerClass, BeanDefinition.SCOPE_SINGLETON);
	}

	@Override
	public void registerController(Class<?> controllerClass, String scope) {
		this.registerController(controllerClass, scope, false);
	}

	@Override
	public void registerController(Class<?> controllerClass, String scope, boolean lazyInit) {
		this.registerController(controllerClass, scope, lazyInit, true);
	}

	@Override
	public void registerController(Class<?> controllerClass, String scope, boolean lazyInit,
			boolean autowireCandidate) {
		this.registerController(controllerClass.getName(), controllerClass, scope, lazyInit, autowireCandidate);
	}

	@Override
	public void registerController(String beanName, Class<?> controllerClass) {
		this.registerController(beanName, controllerClass, BeanDefinition.SCOPE_SINGLETON);
	}

	@Override
	public void registerController(String beanName, Class<?> controllerClass, String scope) {
		this.registerController(beanName, controllerClass, scope, false);
	}

	@Override
	public void registerController(String beanName, Class<?> controllerClass, String scope, boolean lazyInit) {
		this.registerController(beanName, controllerClass, scope, lazyInit, true);
	}

	@Override
	public void registerController(String beanName, Class<?> controllerClass, String scope, boolean lazyInit,
			boolean autowireCandidate) {

		Assert.notNull(controllerClass, "register controller bean class must not null");
		if (!WebApplicationContext.class.isAssignableFrom(getApplicationContext().getClass())) {
			throw new IllegalArgumentException("applicationContext must be WebApplicationContext type");
		}

		// 构造Controller BeanDefinition
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(controllerClass);
		bd.setScope(scope);
		bd.setLazyInit(lazyInit);
		bd.setAutowireCandidate(autowireCandidate);

		this.registerController(beanName, bd);

	}

	@Override
	public void registerController(String beanName, BeanDefinition beanDefinition) {

		Assert.notNull(beanDefinition, "beanDefinition must not null");
		if (!WebApplicationContext.class.isAssignableFrom(getApplicationContext().getClass())) {
			throw new IllegalArgumentException("applicationContext must be WebApplicationContext type");
		}

		beanName = StringUtils.isEmpty(beanName) ? beanDefinition.getBeanClassName() : beanName;

		// 1、如果RequestMapping存在则移除
		removeRequestMappingIfNecessary(beanName);
		// 2、注册新的Controller
		getBeanFactory().registerBeanDefinition(beanName, beanDefinition);
		// 3、注册新的RequestMapping
		registerRequestMappingIfNecessary(beanName);

	}
	
	@Override
	public void registerController(String beanName, Object controller) {
		
		Assert.notNull(controller, "controller must not null");
		if (!WebApplicationContext.class.isAssignableFrom(getApplicationContext().getClass())) {
			throw new IllegalArgumentException("applicationContext must be WebApplicationContext type");
		}

		beanName = StringUtils.isEmpty(beanName) ? controller.getClass().getName() : beanName;

		// 1、如果RequestMapping存在则移除
		removeRequestMappingIfNecessary(beanName);
		// 2、注册新的Controller
		getBeanFactory().registerSingleton(beanName, controller);
		// 3、注册新的RequestMapping
		registerRequestMappingIfNecessary(beanName);
	}

	@Override
	public void removeController(String controllerBeanName) throws IOException {
		// 如果RequestMapping存在则移除
		removeRequestMappingIfNecessary(controllerBeanName);
	}

	@SuppressWarnings("unchecked")
	protected void removeRequestMappingIfNecessary(String controllerBeanName) {

		if (!getBeanFactory().containsBean(controllerBeanName)) {
			return;
		}

		RequestMappingHandlerMapping requestMappingHandlerMapping = getRequestMappingHandlerMapping();

		// remove old
		Class<?> handlerType = getApplicationContext().getType(controllerBeanName);
		final Class<?> userType = ClassUtils.getUserClass(handlerType);

		/*
		 * Map<RequestMappingInfo, HandlerMethod> handlerMethods =
		 * requestMappingHandlerMapping.getHandlerMethods(); 返回只读集合：
		 * 特别说明：因requestMappingHandlerMapping.getHandlerMethods()方法获取到的结果是只读集合，不能进行移除操作，
		 * 所以需要采用反射方式获取目标对象
		 */
		Object mappingRegistry = ReflectionUtils.getField(mappingRegistryField, requestMappingHandlerMapping);
		Method getMappingsMethod = ReflectionUtils.findMethod(mappingRegistry.getClass(), "getMappings");
		getMappingsMethod.setAccessible(true);
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = (Map<RequestMappingInfo, HandlerMethod>) ReflectionUtils
				.invokeMethod(getMappingsMethod, mappingRegistry);

		/*
		 * 查找URL映射：解决 Ambiguous handler methods mapped for HTTP path “” 问题
		 */
		Field urlLookupField = ReflectionUtils.findField(mappingRegistry.getClass(), "urlLookup");
		urlLookupField.setAccessible(true);
		MultiValueMap<String, RequestMappingInfo> urlMapping = (MultiValueMap<String, RequestMappingInfo>) ReflectionUtils
				.getField(urlLookupField, mappingRegistry);

		final RequestMappingHandlerMapping innerRequestMappingHandlerMapping = requestMappingHandlerMapping;
		Set<Method> methods = MethodIntrospector.selectMethods(userType, new ReflectionUtils.MethodFilter() {
			@Override
			public boolean matches(Method method) {
				return ReflectionUtils.invokeMethod(getMappingForMethodMethod, innerRequestMappingHandlerMapping,
						method, userType) != null;
			}
		});

		for (Method method : methods) {

			RequestMappingInfo requestMappingInfo = (RequestMappingInfo) ReflectionUtils
					.invokeMethod(getMappingForMethodMethod, requestMappingHandlerMapping, method, userType);

			handlerMethods.remove(requestMappingInfo);

			PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
			Set<String> patterns = patternsCondition.getPatterns();
			// (Set<String>) ReflectionUtils.invokeMethod(getMappingPathPatternsMethod,
			// requestMappingHandlerMapping, mapping);

			PathMatcher pathMatcher = requestMappingHandlerMapping.getPathMatcher();
			// (PathMatcher) ReflectionUtils.invokeMethod(getPathMatcherMethod,
			// requestMappingHandlerMapping);

			for (String pattern : patterns) {
				if (!pathMatcher.isPattern(pattern)) {
					urlMapping.remove(pattern);
				}
			}
		}

	}

	protected void registerRequestMappingIfNecessary(String controllerBeanName) {

		RequestMappingHandlerMapping requestMappingHandlerMapping = getRequestMappingHandlerMapping();
		// spring 3.1 开始
		ReflectionUtils.invokeMethod(detectHandlerMethodsMethod, requestMappingHandlerMapping, controllerBeanName);

	} 
	@SuppressWarnings("unchecked")
	protected Map<String, InjectionMetadata> getInjectionMetadataCache() {

		AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor = getApplicationContext()
				.getBean(AutowiredAnnotationBeanPostProcessor.class);

		Map<String, InjectionMetadata> injectionMetadataMap = (Map<String, InjectionMetadata>) ReflectionUtils
				.getField(injectionMetadataCacheField, autowiredAnnotationBeanPostProcessor);

		return injectionMetadataMap;
	}

	protected RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		try {
			
			if (requestMappingHandlerMapping != null) {
				return requestMappingHandlerMapping;
			}
			
			Map<String, RequestMappingHandlerMapping> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
					getApplicationContext(), RequestMappingHandlerMapping.class, true, false);
			if (!beans.isEmpty()) {
				List<RequestMappingHandlerMapping> mappings = new ArrayList<>(beans.values());
				for(RequestMappingHandlerMapping handlerMapping : beans.values()) {
					if(handlerMapping.getClass().getName().equals(RequestMappingHandlerMapping.class.getName())) {
						requestMappingHandlerMapping = handlerMapping;
						return handlerMapping;
					}
				}
				AnnotationAwareOrderComparator.sort(mappings);
				requestMappingHandlerMapping = mappings.get(0);
				return requestMappingHandlerMapping;
			}
			requestMappingHandlerMapping = getApplicationContext().getBean(RequestMappingHandlerMapping.class);
			return requestMappingHandlerMapping;
		} catch (Exception e) {
			throw new IllegalArgumentException("applicationContext must has RequestMappingHandlerMapping");
		}
	}

}