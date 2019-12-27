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

import org.springframework.beans.BeansException;
import org.springframework.biz.utils.SpringContextUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>ApplicationContext 注入型上下文实例（用于取spring容器中定义的bean）</p>
 * <b>Example:</b>
 * <pre>
 * 配置：
 * &lt;bean id="springAwareContext" class="org.springframework.biz.context.SpringContextAwareContext"/&gt;
 * </pre>
 */
public class SpringContextAwareContext extends AbstractSpringInstanceContext implements ApplicationContextAware{
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		super.setApplicationContext(applicationContext);
		SpringContextUtils.setContext(this);
		LOG.info("SpringContextAwareContext initialization completed.");
	}

}
