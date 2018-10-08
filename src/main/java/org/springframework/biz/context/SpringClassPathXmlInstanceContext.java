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
package org.springframework.biz.context;

import org.springframework.biz.utils.SpringContextUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @className	： SpringClassPathXmlInstanceContext
 * @description	： Spring 类路径XML配置上下文实例
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年4月18日 下午8:58:45
 * @version 	V1.0
 */
public class SpringClassPathXmlInstanceContext extends AbstractSpringInstanceContext {

	public SpringClassPathXmlInstanceContext(String[] locations) {
		if (locations.length < 1) {
			return;
		} else {
			super.setApplicationContext(new ClassPathXmlApplicationContext(locations));
			SpringContextUtils.setContext(this);
			LOG.info("SpringClassPathXmlInstanceContext initialization completed.");
			return;
		}
	}
}
