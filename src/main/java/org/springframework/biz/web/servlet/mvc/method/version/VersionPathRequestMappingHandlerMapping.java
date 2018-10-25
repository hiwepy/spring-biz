/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.biz.web.servlet.mvc.method.annotation.HyperRequestMappingHandlerMapping;
import org.springframework.web.method.HandlerMethod;

/**
 * 参考： http://www.itkeyword.com/doc/1092001740570865187/how-to-manage-rest-api-versioning-with-spring
 * <pre>
 * 如果当前版本的方法没有找到,则查询上一个版本的方法（解决部分接口版本升级,客户端采用统一调用方式问题）
 * 
 * 假设有接口：
 * 
 * http://localhost:9001/api/v1/user
 * http://localhost:9001/api/v1/company
 * 
 * 某天接口2升级了：
 *  
 * http://localhost:9001/api/v2/company
 * 
 * 客户端此时可以采用相同版本号v2来方法原来的接口和升级后的接口
 * 
 * http://localhost:9001/api/v2/user >> http://localhost:9001/api/v1/user
 * http://localhost:9001/api/v2/company
 * </pre>
 */
public class VersionPathRequestMappingHandlerMapping extends HyperRequestMappingHandlerMapping {

	private final String prefix;

	public VersionPathRequestMappingHandlerMapping(String prefix, String lookupParamName) {
		super(lookupParamName);
		this.prefix = prefix;
	}

	@Override
	protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
		HandlerMethod method = super.lookupHandlerMethod(lookupPath, request);
		if (method == null && lookupPath.contains(getPrefix())) {
			// http://localhost:{port}/{context-path}/{prefix}/path
			// http://localhost:{port}/{context-path}/v1/user
			String afterAPIURL = lookupPath.substring(lookupPath.indexOf(getPrefix()) + getPrefix().length());
			String version = afterAPIURL.substring(0, afterAPIURL.indexOf("/"));
			String path = afterAPIURL.substring(version.length() + 1);
			// 计算上一个版本
			int previousVersion = getPreviousVersion(version);
			if (previousVersion != 0) {
				lookupPath = getPrefix() + previousVersion + "/" + path;
				final String lookupFinal = lookupPath;
				// 递归查询是否定义了上个版本的方法
				return lookupHandlerMethod(lookupPath, new HttpServletRequestWrapper(request) {

					@Override
					public String getRequestURI() {
						return lookupFinal;
					}

					@Override
					public String getServletPath() {
						return lookupFinal;
					}
					
				});
			}
		}
		return method;
	}

	public String getPrefix() {
		return prefix;
	}

	private int getPreviousVersion(final String version) {
		return new Integer(version) - 1;
	}
}