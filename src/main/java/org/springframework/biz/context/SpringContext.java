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
	 * 根据对象类型获取服务对象
	 * @param requiredType ： 目标对象类型
	 * @param <T> 对象泛型
	 * @return 对象
	 */
	public abstract <T> T getInstance(Class<T> requiredType);
	
	/**
	 * 根据服务名获取服务对象
	 * @param name ： 目标对象名称
	 * @return 对象 
	 */
	public abstract Object getInstance(String name);
	
	/**
	 * 获取名称为 dataSource 的数据源对象
	 * @return {@link DataSource} instance
	 */
	public abstract DataSource getDataSource();
	
	/**
	 * 根据数据源名称获取数据源对象
	 * @param datasourceName : 数据源名称
	 * @return {@link DataSource} instance
	 */
	public abstract DataSource getDataSource(String datasourceName);
	
	public abstract void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
	
	public abstract ApplicationContext getApplicationContext();
	
}
