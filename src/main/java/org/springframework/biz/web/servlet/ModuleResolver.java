package org.springframework.biz.web.servlet;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;

public interface ModuleResolver {

	String resolveModule(HttpServletRequest request);

	void setModule(HttpServletRequest request, HttpServletResponse response, String moduleName);

	
}
