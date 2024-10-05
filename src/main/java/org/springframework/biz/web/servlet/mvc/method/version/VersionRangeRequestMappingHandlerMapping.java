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
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * https://github.com/augusto/restVersioning
 */
public class VersionRangeRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	@Override
	protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
		VersionRangeMapping typeAnnotation = AnnotationUtils.findAnnotation(handlerType, VersionRangeMapping.class);
		return createCondition(typeAnnotation);
	}

	@Override
	protected RequestCondition<?> getCustomMethodCondition(Method method) {
		VersionRangeMapping methodAnnotation = AnnotationUtils.findAnnotation(method, VersionRangeMapping.class);
		return createCondition(methodAnnotation);
	}

	private RequestCondition<?> createCondition(VersionRangeMapping versionMapping) {
		if (versionMapping != null) {
			return new VersionRangeMappingRequestCondition(versionMapping.media(), versionMapping.from(), versionMapping.to());
		}
		return null;
	}

}
