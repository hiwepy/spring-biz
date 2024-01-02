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
package org.springframework.biz.web.servlet.mvc.method.version;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * http://www.itkeyword.com/doc/1092001740570865187/how-to-manage-rest-api-versioning-with-spring
 */
public class VersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	private final String prefix;

	public VersionRequestMappingHandlerMapping(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
		if (info == null) {
			return null;
		}
		VersionMapping methodAnnotation = AnnotationUtils.findAnnotation(method, VersionMapping.class);
		if (methodAnnotation != null) {
			RequestCondition<?> methodCondition = getCustomMethodCondition(method);
			// Concatenate our VersionMapping with the usual request mapping
			info = createRequestMappingInfo(methodAnnotation, methodCondition).combine(info);
		} else {
			VersionMapping typeAnnotation = AnnotationUtils.findAnnotation(handlerType, VersionMapping.class);
			if (typeAnnotation != null) {
				RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
				// Concatenate our VersionMapping with the usual request mapping
				info = createRequestMappingInfo(typeAnnotation, typeCondition).combine(info);
			}
		}

		return info;
	}

	protected RequestMappingInfo createRequestMappingInfo(VersionMapping annotation, RequestCondition<?> customCondition) {
		int[] values = annotation.value();
		String[] patterns = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			// Build the URL prefix
			patterns[i] = prefix + values[i];
		}

		return new RequestMappingInfo(
				new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(), useSuffixPatternMatch(),
						useTrailingSlashMatch(), getFileExtensions()),
				new RequestMethodsRequestCondition(), new ParamsRequestCondition(), new HeadersRequestCondition(),
				new ConsumesRequestCondition(), new ProducesRequestCondition(), customCondition);
	}

}
