package org.springframework.biz.web.servlet.mvc.registry;

import org.springframework.scripting.groovy.GroovyScriptFactory;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDynamicGroovyControllerRegistry extends DefaultDynamicControllerRegistry
		implements DynamicGroovyControllerRegistry {


	private Map<String, Long> scriptLastModifiedMap = new ConcurrentHashMap<String, Long>();// in millis

	public DefaultDynamicGroovyControllerRegistry() {
		this(-1L);
	}
	
	public DefaultDynamicGroovyControllerRegistry(Long scriptCheckInterval) {
		if (scriptCheckInterval > 0L) {
			startScriptModifiedCheckThead(scriptCheckInterval);
		}
	}

	@Override
	public void registerGroovyController(String scriptLocation) throws IOException {

		if (scriptNotExists(scriptLocation)) {
			throw new IllegalArgumentException("script not exists : " + scriptLocation);
		}
		scriptLastModifiedMap.put(scriptLocation, scriptLastModified(scriptLocation));

		// Create script factory bean definition.
		GroovyScriptFactory groovyScriptFactory = new GroovyScriptFactory(scriptLocation);
		groovyScriptFactory.setBeanFactory(getBeanFactory());
		groovyScriptFactory.setBeanClassLoader(getBeanFactory().getBeanClassLoader());
		Object controller = groovyScriptFactory
				.getScriptedObject(new ResourceScriptSource(getApplicationContext().getResource(scriptLocation)));

		String controllerBeanName = scriptLocation;

		// 1、如果RequestMapping存在则移除
		removeRequestMappingIfNecessary(controllerBeanName);
		if (getBeanFactory().containsBean(controllerBeanName)) {
			getBeanFactory().destroySingleton(controllerBeanName); // 移除单例bean
			// 移除注入缓存 否则Caused by: java.lang.IllegalArgumentException: object is not an
			// instance of declaring class
			getInjectionMetadataCache().remove(controller.getClass().getName());
		}

		// 2、注册新的GroovyController
		getBeanFactory().registerSingleton(controllerBeanName, controller); // 注册单例bean
		getBeanFactory().autowireBean(controller); // 自动注入

		// 3、注册新的RequestMapping
		registerRequestMappingIfNecessary(controllerBeanName);
	}

	@Override
	public void removeGroovyController(String scriptLocation, String controllerBeanName) throws IOException {

		if (scriptNotExists(scriptLocation)) {
			throw new IllegalArgumentException("script not exists : " + scriptLocation);
		}

		// 如果RequestMapping存在则移除
		removeRequestMappingIfNecessary(scriptLocation);
		if (getBeanFactory().containsBean(scriptLocation)) {
			getBeanFactory().destroySingleton(scriptLocation); // 移除单例bean
			// 移除注入缓存 否则Caused by: java.lang.IllegalArgumentException: object is not an
			// instance of declaring class
			getInjectionMetadataCache().remove(controllerBeanName);
		}

	}

	private void startScriptModifiedCheckThead(final Long scriptCheckInterval) {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {

						Thread.sleep(scriptCheckInterval);

						Map<String, Long> copyMap = new HashMap<String, Long>(scriptLastModifiedMap);
						for (String scriptLocation : copyMap.keySet()) {

							if (scriptNotExists(scriptLocation)) {
								scriptLastModifiedMap.remove(scriptLocation);
								// TODO remove handler mapping ?
							}
							if (copyMap.get(scriptLocation) != scriptLastModified(scriptLocation)) {
								registerGroovyController(scriptLocation);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						// ignore
					}
				}
			}
		}.start();
	}
 
	protected long scriptLastModified(String scriptLocation) {
		try {
			return getApplicationContext().getResource(scriptLocation).getFile().lastModified();
		} catch (Exception e) {
			return -1;
		}
	}

	protected boolean scriptNotExists(String scriptLocation) {
		return !getApplicationContext().getResource(scriptLocation).exists();
	}

}