package org.springframework.biz.web.servlet.module;

import org.springframework.biz.web.servlet.ModuleResolver;

public abstract class AbstractModuleResolver implements ModuleResolver {

	/**
	 * Out-of-the-box value for the default module name: "default".
	 */
	public final static String ORIGINAL_DEFAULT_MODULE_NAME = "default";

	private String defaultModule = ORIGINAL_DEFAULT_MODULE_NAME;


	/**
	 * Set the name of the default module. Out-of-the-box value is "default".
	 * @return default module
	 */
	public String getDefaultModule() {
		return defaultModule;
	}
	
	/**
	 * Return the name of the default module.
	 * @param defaultModule default module
	 */
	public void setDefaultModule(String defaultModule) {
		this.defaultModule = defaultModule;
	}
	
}
