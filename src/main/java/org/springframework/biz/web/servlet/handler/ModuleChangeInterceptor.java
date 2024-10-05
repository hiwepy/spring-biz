package org.springframework.biz.web.servlet.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModuleChangeInterceptor extends WebRequestHandlerInterceptorAdapter {

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
	 * Create a new WebRequestHandlerInterceptorAdapter for the given WebRequestInterceptor.
	 *
	 * @param requestInterceptor the WebRequestInterceptor to wrap
	 */
	public ModuleChangeInterceptor(WebRequestInterceptor requestInterceptor) {
		super(requestInterceptor);
	}

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
