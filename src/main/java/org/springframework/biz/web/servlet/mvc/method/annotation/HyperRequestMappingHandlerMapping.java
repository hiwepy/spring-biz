package org.springframework.biz.web.servlet.mvc.method.annotation;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.biz.web.DuplicateHandlerMethodException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * https://tech.imdada.cn/2015/12/23/springmvc-restful-optimize/
 * https://www.jianshu.com/p/5574cb427140
 */
public class HyperRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	private final static Map<HandlerMethod, RequestMappingInfo> HANDLER_METHOD_REQUEST_MAPPING_INFO_MAP = new ConcurrentHashMap<>();
	private String lookupParamName = "mapping";

	public HyperRequestMappingHandlerMapping(String lookupParamName) {
		this.lookupParamName = lookupParamName;
	}
	
	/*
	 * 用于保存处理方法和RequestMappingInfo的映射关系(这个方法在解析@RequestMapping时就会被调用,
	 * 达达科技中这个地方可能写的有问题, 文中提到覆写AbstractHandlerMethodMapping#registerMapping方法,但是经过实验之后覆写这个方法不能生效)
	 * @see org.springframework.web.servlet.handler.AbstractHandlerMethodMapping#registerHandlerMethod(java.lang.Object, java.lang.reflect.Method, java.lang.Object)
	 */
	@Override
	protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
		HandlerMethod handlerMethod = super.createHandlerMethod(handler, method);
		HANDLER_METHOD_REQUEST_MAPPING_INFO_MAP.put(handlerMethod, mapping);
		super.registerHandlerMethod(handler, method, mapping);
	}

	@Override
	protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
		// 判断请求参数中是否带了指定的字段
		String mapping = request.getParameter(lookupParamName);
		// 如果没有带则说明这次的请求不带路径参数, 则使用默认的处理
		if (StringUtils.isEmpty(mapping)) {
			return super.lookupHandlerMethod(lookupPath, request);
		}
		// 如果带了, 则从Map(这个Map中的entry在后面介绍)中获取处理当前url的方法
		List<HandlerMethod> handlerMethods = super.getHandlerMethodsForMappingName(mapping);
		if (CollectionUtils.isEmpty(handlerMethods)) {
			// 返回null交个其他可能的RequestMappingHandlerMapping进行处理
			return null;
		}
		if (handlerMethods.size() > 1) {
			throw new DuplicateHandlerMethodException(mapping , handlerMethods);
		}
		HandlerMethod handlerMethod = handlerMethods.get(0);
		// 根据处理方法查找RequestMappingInfo, 用于解析路径url中的参数
		RequestMappingInfo requestMappingInfo = HANDLER_METHOD_REQUEST_MAPPING_INFO_MAP.get(handlerMethod);
		if (requestMappingInfo == null) {
			// 返回null交个其他可能的RequestMappingHandlerMapping进行处理
			return null;
		}
		super.handleMatch(requestMappingInfo, lookupPath, request);
		return handlerMethod;
	}

}
