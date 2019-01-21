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
package org.springframework.biz.utils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

public class WebRequestUtils {

	protected static Logger LOG = LoggerFactory.getLogger(WebRequestUtils.class);

	/** 
     * Part of HTTP content type header.
     */
    public static final String MULTIPART = "multipart/";
    
	/**
     * Test if current HttpServletRequest is a multipart request.
     * @param request The HttpServletRequest
     * @return if current HttpServletRequest is a multipart request.
     */
    public static boolean isMultipartRequest(HttpServletRequest request) {
    	if (!"post".equals(request.getMethod().toLowerCase())) {
            return false;
        }
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        if (contentType.toLowerCase().startsWith(MULTIPART)) {
            return true;
        }
        return false;
    }

	public static String buildURL(String baseURL, Map<String, Object> paramsMap) {
		if (paramsMap == null) {
			return baseURL;
		}
		StringBuilder builder = new StringBuilder(baseURL);
		for (String key : paramsMap.keySet()) {
			builder.append(builder.indexOf("?") > 0 ? "&" : "?").append(key).append("=")
					.append(String.valueOf(paramsMap.get(key)));
		}
		String newUrl = builder.toString();
		System.out.println(newUrl);
		return newUrl;
	}

	public static Map<String, String> headerMap(HttpServletRequest request) {

		// 参见网络资料：https://blog.csdn.net/m0_37711172/article/details/79724560
		Map<String, String> headerMap = new HashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			// 导致请求报错，需要排除
			if (HttpHeaders.HOST.equalsIgnoreCase(name)) {
				continue;
			}
			// okhttp请求数据乱码问题: https://blog.csdn.net/chinajpr/article/details/54892751
			if (HttpHeaders.ACCEPT_ENCODING.equalsIgnoreCase(name)) {
				continue;
			}
			headerMap.put(name, request.getHeader(name));
		}
		return headerMap;
	}

	public static MultiValueMap<String, String> multiHeaderMap(HttpServletRequest request) {

		MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			// 导致请求报错，需要排除
			if (HttpHeaders.HOST.equalsIgnoreCase(name)) {
				continue;
			}
			headerMap.add(name, request.getHeader(name));
		}
		return headerMap;
	}

	public static HttpEntity<MultiValueMap<String, Object>> requestEntity(HttpServletRequest request,
			Map<String, Object> params) throws IOException {

		HttpHeaders headers = new HttpHeaders();
		headers.addAll(multiHeaderMap(request));
		headers.setContentType(MediaType.parseMediaType(request.getContentType()));

		MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<String, Object>();
		// 文件和参数请求方法
		if (isMultipartRequest(request)) {
			for (String key : params.keySet()) {
				// 文件参数
				if (params.get(key) instanceof MultipartFile) {
					MultipartFile file = (MultipartFile) params.get(key);
					requestBody.add(file.getName(), new InputStreamResource(file.getInputStream()));
				}
				// 普通参数
				else {
					requestBody.add(key, String.valueOf(params.get(key)));
				}
			}
		}
		// 普通请求参数方法
		else {
			// Form参数
			for (String key : params.keySet()) {
				requestBody.add(key, String.valueOf(params.get(key)));
			}
		}
		return new HttpEntity<>(requestBody, headers);
	}

	
}

