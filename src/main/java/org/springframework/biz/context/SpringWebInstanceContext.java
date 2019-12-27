/*
 * Copyright (c) 2018 (https://github.com/hiwepy).
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
package org.springframework.biz.context;

import javax.servlet.ServletContext;

import org.springframework.biz.utils.SpringContextUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * WebApplicationContext上下文实例;根据ServletContext初始化的实例
 */
public class SpringWebInstanceContext extends AbstractSpringInstanceContext{

	public static void initialInstanceContext(ServletContext servletContext) {
		new SpringWebInstanceContext(WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext));
	}

	private SpringWebInstanceContext(ApplicationContext applicationContext) {
		super.setApplicationContext(applicationContext);
		SpringContextUtils.setContext(this);
		LOG.info("SpringWebInstanceContext initialization completed.");
	}
}
