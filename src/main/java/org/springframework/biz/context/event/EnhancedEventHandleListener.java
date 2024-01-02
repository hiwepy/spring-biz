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
package org.springframework.biz.context.event;

import org.springframework.biz.context.event.aspect.AbstractJoinPointEvent;
import org.springframework.biz.context.event.handler.EventHandler;
import org.springframework.biz.context.event.handler.ExceptionEventHandler;
import org.springframework.biz.factory.EnhancedBeanFactory;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Method;
import java.util.Map;

/**
 *  <p>Spring中ApplicationContext的事件机制--- 内定事件)</p>
 *  <b>在Spring中已经定义了五个标准事件，分别介绍如下：</b>
 *  <p>1)、ContextRefreshedEvent：当ApplicationContext初始化或者刷新时触发该事件。</p>
 *  <p>2)、ContextClosedEvent：当ApplicationContext被关闭时触发该事件。容器被关闭时，其管理的所有单例Bean都被销毁。</p>
 *  <p>3)、RequestHandleEvent：在Web应用中，当一个http请求（request）结束触发该事件。</p>
 *  <p>4)、ContestStartedEvent：Spring2.5新增的事件，当容器调用ConfigurableApplicationContext的Start()方法开始/重新开始容器时触发该事件。</p>
 *  <p>5)、ContestStopedEvent：Spring2.5新增的事件，当容器调用ConfigurableApplicationContext的Stop()方法停止容器时触发该事件。</p>
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class EnhancedEventHandleListener extends EnhancedBeanFactory implements ApplicationListener<EnhancedEvent> {

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
	
	@Override
	public void onApplicationEvent(EnhancedEvent event) {
		// 异常事件推送
		if (event instanceof ExceptionEvent) {
			//获取所有异常处理接口实现
			Map<String, ExceptionEventHandler>  handlers = getApplicationContext().getBeansOfType(ExceptionEventHandler.class);
			if( null != handlers){
				for (String alias : handlers.keySet()) {
					handlers.get(alias).handle((ExceptionEvent)event);
				}
			}
		}
		// JoinPoint 事件推送
		else if (event instanceof AbstractJoinPointEvent) {
			//类型转换
			AbstractJoinPointEvent joinPointEvent =  (AbstractJoinPointEvent) event;
			//获取事件环境对象
			EventInvocation invocation = joinPointEvent.getBind();
			//当前访问的方法
			Method method = invocation.getMethod();
			//事件切面注解
			EventAspect eventAspect = method.getAnnotation(EventAspect.class);
			if( null != eventAspect){
				//获取指定的所有事件处理接口实现
				Map<String, ?>  handlers = getApplicationContext().getBeansOfType(eventAspect.handler());
				if( null != handlers){
					for (String alias : handlers.keySet()) {
						((EventHandler)handlers.get(alias)).handle(event);
					}
				}
			}
		}
	}
	
}