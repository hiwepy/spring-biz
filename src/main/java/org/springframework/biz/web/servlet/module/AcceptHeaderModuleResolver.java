package org.springframework.biz.web.servlet.module;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AcceptHeaderModuleResolver extends AbstractModuleResolver {

	@Override
	public String resolveModule(HttpServletRequest request) {
		String module = request.getHeader("Accept-Module");
		if(module != null){
			return module;
		}
		return getDefaultModule();
	}

	@Override
	public void setModule(HttpServletRequest request, HttpServletResponse response, String moduleName) {
		throw new UnsupportedOperationException(
				"Cannot change HTTP accept header - use a different module resolution strategy");
	}

}
