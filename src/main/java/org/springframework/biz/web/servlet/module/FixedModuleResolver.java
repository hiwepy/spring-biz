package org.springframework.biz.web.servlet.module;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FixedModuleResolver extends AbstractModuleResolver {

	@Override
	public String resolveModule(HttpServletRequest request) {
		return getDefaultModule();
	}

	@Override
	public void setModule(HttpServletRequest request, HttpServletResponse response, String moduleName) {
		throw new UnsupportedOperationException("Cannot change module - use a different module resolution strategy");
	}

}
