package org.springframework.biz.web.servlet.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 为你的REST API添加CORS支持
 * 当访问REST API时，你可能需要面对“同源策略”问题。
 * 错误如下：
 * ” No ‘Access-Control-Allow-Origin’ header is present on the requested resource. Origin ‘http://127.0.0.1:8080′ is therefore not allowed access.” OR
 * ” XMLHttpRequest cannot load http://abc.com/bla. Origin http://localhost:12345 is not allowed by Access-Control-Allow-Origin.”
 */
public class SpringMVCCORSInterceptor implements HandlerInterceptor {
	
	private String allowOrigin = "*";
	private String allowMethods = "POST, GET, PUT, OPTIONS, DELETE";
	private String allowHeaders = "Origin, X-Requested-With, Content-Type, Accept";
	private String maxAge = "3600";
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		response.setHeader("Access-Control-Allow-Origin", getAllowOrigin());  
        response.setHeader("Access-Control-Allow-Methods", getAllowMethods());  
        response.setHeader("Access-Control-Allow-Headers", getAllowHeaders());  
        response.setHeader("Access-Control-Max-Age", getMaxAge());

	}

	public String getAllowOrigin() {
		return allowOrigin;
	}

	public void setAllowOrigin(String allowOrigin) {
		this.allowOrigin = allowOrigin;
	}

	public String getAllowMethods() {
		return allowMethods;
	}

	public void setAllowMethods(String allowMethods) {
		this.allowMethods = allowMethods;
	}

	public String getAllowHeaders() {
		return allowHeaders;
	}

	public void setAllowHeaders(String allowHeaders) {
		this.allowHeaders = allowHeaders;
	}

	public String getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}

}
