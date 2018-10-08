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

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public interface SpringContext {

	/**
	 * 
	 * @description	： 根据对象类型获取服务对象
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
	 * @date 		：2017年4月18日 下午8:57:33
	 * @param requiredType
	 * @return
	 */
	public abstract <T> T getInstance(Class<T> requiredType);
	
	/**
	 * 
	 * @description	： 根据服务名获取服务对象
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
	 * @date 		：2017年4月18日 下午8:57:42
	 * @param name
	 * @return
	 */
	public abstract Object getInstance(String name);
	
	/**
	 * 
	 * @description	： 获取名称为 dataSource 的数据源对象
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
	 * @date 		：2017年4月18日 下午8:57:51
	 * @return
	 */
	public abstract DataSource getDataSource();
	
	/**
	 * 
	 * @description	： 根据数据源名称获取数据源对象
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
	 * @date 		：2017年4月18日 下午8:57:59
	 * @param datasourceName
	 * @return
	 */
	public abstract DataSource getDataSource(String datasourceName);
	
	public abstract void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
	
	public abstract ApplicationContext getApplicationContext();
	
}
