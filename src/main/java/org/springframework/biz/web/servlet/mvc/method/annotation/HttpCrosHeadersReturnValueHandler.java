package org.springframework.biz.web.servlet.mvc.method.annotation;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 在某域名下使用Ajax向另一个域名下的页面请求数据，会遇到跨域问题。
 * 另一个域名必须在response中添加 Access-Control-Allow-Origin 的header，才能让前者成功拿到数据。
 */
public class HttpCrosHeadersReturnValueHandler implements HandlerMethodReturnValueHandler {

	private String allowOrigin = "*";
	private String allowMethods = "POST, GET, PUT, OPTIONS, DELETE";
	private String allowHeaders = "X-Requested-With";
	private String maxAge = "3600";
	
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		//仅仅支持 Rest API
		return returnType.getDeclaringClass().getAnnotation(RestController.class) != null;
	}

	@Override
	@SuppressWarnings("resource")
	public void handleReturnValue(Object returnValue, MethodParameter returnType,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

		mavContainer.setRequestHandled(true);
		
		HttpServletResponse servletResponse = webRequest.getNativeResponse(HttpServletResponse.class);
		ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(servletResponse);
		
		outputMessage.getHeaders().set("Access-Control-Allow-Origin", getAllowOrigin());  
        outputMessage.getHeaders().set("Access-Control-Allow-Methods", getAllowMethods());  
        outputMessage.getHeaders().set("Access-Control-Allow-Headers", getAllowHeaders());  
        outputMessage.getHeaders().set("Access-Control-Max-Age", getMaxAge());
		
		outputMessage.getBody(); // flush headers
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
