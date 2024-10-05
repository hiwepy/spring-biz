/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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
package org.springframework.biz.web;

import org.springframework.web.method.HandlerMethod;

import javax.servlet.ServletException;
import java.util.List;

@SuppressWarnings("serial")
public class DuplicateHandlerMethodException extends ServletException {

	private String mapping;
	private List<HandlerMethod> handlerMethods;

	public DuplicateHandlerMethodException(String mapping, List<HandlerMethod> handlerMethods) {
		super("Duplicate HandlerMethod for key '" + mapping + "'");
		this.mapping = mapping;
		this.handlerMethods = handlerMethods;
	}
	
	public DuplicateHandlerMethodException(String mapping, List<HandlerMethod> handlerMethods, Throwable rootCause) {
		super("Duplicate HandlerMethod for key '" + mapping + "'", rootCause);
		this.mapping = mapping;
		this.handlerMethods = handlerMethods;
	}
	
	public String getMapping() {
		return mapping;
	}

	public List<HandlerMethod> getHandlerMethods() {
		return handlerMethods;
	}

}
