package org.springframework.biz.web.servlet.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

public class ModuleChangeInterceptor implements HandlerInterceptor {

	/**
	 * Name of the session attribute that holds the module.
	 */
	public static final String MODULE_SESSION_ATTRIBUTE_NAME = ModuleChangeInterceptor.class.getName() + ".MODULE";

	/**
	 * Default name of the locale specification parameter: "module".
	 */
	public static final String DEFAULT_PARAM_NAME = "module";

	protected final Logger LOG = LoggerFactory.getLogger(getClass());

	private String paramName = DEFAULT_PARAM_NAME;

	private String[] httpMethods;

	/**
	 * Set the name of the parameter that contains a locale specification in a
	 * locale change request. Default is "locale".
	 * @param paramName the paramName
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	/**
	 * Return the name of the parameter that contains a locale specification in
	 * a locale change request.
	 * @return paramName
	 */
	public String getParamName() {
		return this.paramName;
	}

	/**
	 * Configure the HTTP method(s) over which the locale can be changed.
	 * 
	 * @param httpMethods the methods
	 * @since 4.2
	 */
	public void setHttpMethods(String... httpMethods) {
		this.httpMethods = httpMethods;
	}

	/**
	 * Return the configured HTTP methods.
	 * @return HTTP methods
	 * @since 4.2
	 */
	public String[] getHttpMethods() {
		return this.httpMethods;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {

		String newModule = request.getParameter(getParamName());
		if (newModule != null) {
			if (checkHttpMethod(request.getMethod())) {
				WebUtils.setSessionAttribute(request, MODULE_SESSION_ATTRIBUTE_NAME, newModule );
			}
		}
		// Proceed in any case.
		return true;
	}

	private boolean checkHttpMethod(String currentMethod) {
		String[] configuredMethods = getHttpMethods();
		if (ObjectUtils.isEmpty(configuredMethods)) {
			return true;
		}
		for (String configuredMethod : configuredMethods) {
			if (configuredMethod.equalsIgnoreCase(currentMethod)) {
				return true;
			}
		}
		return false;
	}
	
}
