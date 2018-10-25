package org.springframework.biz.web.servlet.mvc.registry;

import java.io.IOException;

public interface DynamicGroovyControllerRegistry {
    
	/**
     * 动态注册Groovy Controller到Spring上下文
     * @param scriptLocation		: The location of groovy script
     * @throws IOException if io error 
     */
	public void registerGroovyController(String scriptLocation) throws IOException;
	
	/**
     * 动态从Spring上下文删除Groovy Controller
     * @param scriptLocation		: The location of groovy script 
     * @param controllerBeanName		: The name of controller 
     * @throws IOException if io error
     */
	public void removeGroovyController(String scriptLocation,String controllerBeanName) throws IOException;
	
}
