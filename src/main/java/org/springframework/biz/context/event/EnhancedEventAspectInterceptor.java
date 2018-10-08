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
package org.springframework.biz.context.event;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.biz.context.event.aspect.JoinPointAfterEvent;
import org.springframework.biz.context.event.aspect.JoinPointAroundEvent;
import org.springframework.biz.context.event.aspect.JoinPointBeforeEvent;
import org.springframework.biz.context.event.aspect.JoinPointThrowingEvent;
import org.springframework.biz.utils.AspectUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.biz.factory.EnhancedBeanFactory;

public class EnhancedEventAspectInterceptor extends EnhancedBeanFactory implements ApplicationEventPublisherAware{
	
	protected ApplicationEventPublisher eventPublisher;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}  

	public void before(JoinPoint point) throws Throwable {
		
		String mathodName = point.getSignature().getName();
		if("setSelf".equalsIgnoreCase(mathodName)){
    		return;
    	}
		
		EventInvocation invocation = new EventInvocation(point);
		
		Signature signature = point.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		invocation.setArgNames(methodSignature.getParameterNames());
		invocation.setArgs(point.getArgs());
		invocation.setMethod(AspectUtils.getMethod(point));
		invocation.setTarget(point.getTarget());
		
		//推送异常事件
 		getEventPublisher().publishEvent(new JoinPointBeforeEvent( this , invocation ));
 		
	}
	
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {  
    	try {
    		
    		Object returnValue = joinPoint.proceed();
    		String mathodName = joinPoint.getSignature().getName();
    		if("setSelf".equalsIgnoreCase(mathodName)){
        		return returnValue;
        	}
    		
    		EventInvocation invocation = new EventInvocation(joinPoint, returnValue);
    		
    		Signature signature = joinPoint.getSignature();
    		MethodSignature methodSignature = (MethodSignature) signature;
    		invocation.setArgNames(methodSignature.getParameterNames());
    		invocation.setArgs(joinPoint.getArgs());
    		invocation.setMethod(AspectUtils.getMethod(joinPoint));
    		invocation.setReturnValue(returnValue);
    		invocation.setTarget(joinPoint.getTarget());
    		
    		//推送异常事件
     		getEventPublisher().publishEvent(new JoinPointAroundEvent( this , invocation ));
     		
     		return returnValue;
     		
        }catch (Exception e) {
            LOG.warn("invoke(ProceedingJoinPoint) - exception ignored", e); //$NON-NLS-1$ 
        }finally {
        	if (LOG.isDebugEnabled()) {
            	LOG.debug("invoke(ProceedingJoinPoint) - end"); //$NON-NLS-1$
            }
        }
        return null; 
    }  
	
    public void afterReturning(JoinPoint point,Object returnValue) throws Throwable {  
    	String mathodName = point.getSignature().getName();
		if("setSelf".equalsIgnoreCase(mathodName)){
    		return;
    	}
		
		EventInvocation invocation = new EventInvocation(point, returnValue);
		
		Signature signature = point.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		invocation.setArgNames(methodSignature.getParameterNames());
		invocation.setArgs(point.getArgs());
		invocation.setMethod(AspectUtils.getMethod(point));
		invocation.setReturnValue(returnValue);
		invocation.setTarget(point.getTarget());
		
		//推送异常事件
 		getEventPublisher().publishEvent(new JoinPointAfterEvent( this , invocation ));
 		
    }  
	
    public void afterThrowing(JoinPoint point,Throwable ex) throws Throwable {  
    	
    	String mathodName = point.getSignature().getName();
		if("setSelf".equalsIgnoreCase(mathodName)){
    		return;
    	}
		
		EventInvocation invocation = new EventInvocation(point, ex);
		
		Signature signature = point.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		invocation.setArgNames(methodSignature.getParameterNames());
		invocation.setArgs(point.getArgs());
		invocation.setMethod(AspectUtils.getMethod(point));
		invocation.setTarget(point.getTarget());
		
		//推送异常事件
 		getEventPublisher().publishEvent(new JoinPointThrowingEvent( this , invocation ));
 		
    }
    
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public ApplicationEventPublisher getEventPublisher() {
		return eventPublisher;
	}
	
}
