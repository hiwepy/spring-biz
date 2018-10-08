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
package org.springframework.biz.context.event.handler;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.biz.context.event.EventInvocation;
import org.springframework.biz.context.event.aspect.JoinPointBeforeEvent;

public class JoinPointBeforeEventHandler implements EventHandler<JoinPointBeforeEvent> {

	protected Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Override
	public void handle(JoinPointBeforeEvent event) {
		
		if (LOG.isDebugEnabled()) {
			// 获取事件环境对象
			EventInvocation invocation = event.getBind();
			JoinPoint point = invocation.getPoint();
			Signature signature = point.getSignature();
			MethodSignature methodSignature = (MethodSignature) signature;

			LOG.debug("JoinPoint Before [ Method ： {} , ArgNames : {} ] ", methodSignature.getName(),
					invocation.getArgNames());

		}
		
	}
	
}
