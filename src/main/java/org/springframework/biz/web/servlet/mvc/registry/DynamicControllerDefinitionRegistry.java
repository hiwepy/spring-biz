package org.springframework.biz.web.servlet.mvc.registry;

import org.springframework.beans.factory.config.BeanDefinition;

public interface DynamicControllerDefinitionRegistry extends DynamicBeanDefinitionRegistry {

	/**
     * 动态注册SpringMVC Controller到Spring上下文
     * @param controllerClass			: The class type of controller 
     */
	public void registerController(Class<?> controllerClass);
	
    /**
     * 动态注册SpringMVC Controller到Spring上下文
     * @param controllerClass	: The class type of controller
     * @param scope				: scope value
     */
    public void registerController(Class<?> controllerClass, String scope); 
    
    /**
     * 动态注册SpringMVC Controller到Spring上下文
     * @param controllerClass	: The class type of controller
     * @param scope				: scope value
     * @param lazyInit			: lazyInit value
     */
    public void registerController(Class<?> controllerClass, String scope, boolean lazyInit); 
    
    /**
     * 动态注册SpringMVC Controller到Spring上下文
     * @param controllerClass	: The class type of controller
     * @param scope				: scope value
     * @param lazyInit			: lazyInit value
     * @param autowireCandidate	: autowireCandidate value
     */
    public void registerController(Class<?> controllerClass, String scope, boolean lazyInit,boolean autowireCandidate); 
	
    /**
     * 动态注册SpringMVC Controller到Spring上下文
     * @param beanName			: The name of bean 
     * @param controllerClass	: The class type of controller
     */
	public void registerController(String beanName, Class<?> controllerClass);
	
    /**
     * 动态注册SpringMVC Controller到Spring上下文
     * @param beanName			: The name of bean 
     * @param controllerClass	: The class type of controller
     * @param scope				: scope value
     */
    public void registerController(String beanName, Class<?> controllerClass, String scope); 
    
    /**
     * 动态注册SpringMVC Controller到Spring上下文
     * @param beanName			: The name of bean 
     * @param controllerClass	: The class type of controller
     * @param scope				: scope value
     * @param lazyInit			: lazyInit value
     */
    public void registerController(String beanName,Class<?> controllerClass, String scope, boolean lazyInit); 
    
    /**
     * 动态注册SpringMVC Controller到Spring上下文
     * @param beanName			: The name of bean 
     * @param controllerClass	: The class type of controller
     * @param scope				: scope value
     * @param lazyInit			: lazyInit value
     * @param autowireCandidate	: autowireCandidate value
     */
    public void registerController(String beanName,Class<?> controllerClass, String scope, boolean lazyInit,boolean autowireCandidate); 

    /**
     * 动态注册SpringMVC Controller到Spring上下文
     * @param beanName			: The name of bean 
     * @param beanDefinition	: {@link BeanDefinition beanDefinition} instance
     */
    public void registerController(String beanName, BeanDefinition beanDefinition);
    
	
}
