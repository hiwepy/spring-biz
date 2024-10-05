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

import org.aspectj.lang.JoinPoint;

import java.lang.reflect.Method;

public class EventInvocation {

	/**
	 * 
	 */
	private JoinPoint point;
	/**
	 * 调用此方法的对象
	 */
	private Object target;
	/**
	 * 调用此方法的方法
	 */
	private Method method;
	/**
	 * 调用此方法的方法的参数名称
	 */
	private String[] argNames;
	/**
	 * 调用此方法的方法的参数
	 */
	private Object[] args;
	/**
	 * 调用此方法的方法的返回值
	 */
	private Object returnValue;
	/**
	 * 异常对象
	 */
	private Throwable throwable;

	public EventInvocation(JoinPoint point) {
		this.point = point;
	}
	
	public EventInvocation(JoinPoint point,Object returnValue) {
		this.point = point;
		this.returnValue = returnValue;
	}
	
	public EventInvocation(JoinPoint point,Throwable throwable) {
		this.point = point;
		this.throwable = throwable;
	}

	public JoinPoint getPoint() {
		return point;
	}

	public void setPoint(JoinPoint point) {
		this.point = point;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String[] getArgNames() {
		return argNames;
	}

	public void setArgNames(String[] argNames) {
		this.argNames = argNames;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
	
}
