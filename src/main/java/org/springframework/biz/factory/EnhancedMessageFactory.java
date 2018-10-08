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
package org.springframework.biz.factory;

import org.springframework.biz.context.support.MultiReloadableResourceBundleMessageSource;
import org.springframework.biz.context.support.MultiResourceBundleMessageSource;
import org.springframework.biz.utils.LocaleUtils;

/**
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public abstract class EnhancedMessageFactory extends EnhancedBeanFactory {

	protected MultiResourceBundleMessageSource staticResource;
	protected MultiReloadableResourceBundleMessageSource reloadableResource;
	
	public String getMessage(String key,String ...patams) {
		return getMessage(key, null, patams);
	}
	
	public String getMessage(String key,String defaultMessage,String ...patams) {
		String message = null;
		//优先使用动态加载的资源
		if (reloadableResource != null) {
			message = reloadableResource.getMessage(key, patams, defaultMessage, LocaleUtils.getLocale());
		}
		//没有取到，再考虑今天不变的资源是否存在
		if(message == null && staticResource != null){
			message = staticResource.getMessage(key, patams, defaultMessage, LocaleUtils.getLocale());
		}
		return message;
	}

	/**
	 * @return the staticResource
	 */
	public MultiResourceBundleMessageSource getStaticResource() {
		return this.staticResource;
	}

	/**
	 * @param staticResource the staticResource to set
	 */
	public void setStaticResource(MultiResourceBundleMessageSource staticResource) {
		this.staticResource = staticResource;
	}
	
	/**
	 * @return the reloadableResource
	 */
	public MultiReloadableResourceBundleMessageSource getReloadableResource() {
		return this.reloadableResource;
	}

	/**
	 * @param reloadableResource the reloadableResource to set
	 */
	public void setReloadableResource(MultiReloadableResourceBundleMessageSource reloadableResource) {
		this.reloadableResource = reloadableResource;
	}
	
}
