/*
 * Copyright (c) 2010-2020, vindell (https://github.com/vindell).
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

import org.springframework.beans.factory.FactoryBean;

public class EnhancedSequenceFactory extends EnhancedBeanFactory implements FactoryBean<Sequence> {

	protected long workerId = -1;

	/* 数据标识id部分 */
	protected long datacenterId = -1;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public Sequence getObject() throws Exception {
		if (workerId > 0 && datacenterId > 0 ) {
			return new Sequence(workerId , datacenterId);
		}
		return new Sequence();
	}

	@Override
	public Class<?> getObjectType() {
		return String.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(long workerId) {
		this.workerId = workerId;
	}

	public long getDatacenterId() {
		return datacenterId;
	}

	public void setDatacenterId(long datacenterId) {
		this.datacenterId = datacenterId;
	}
	

}
