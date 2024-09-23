package org.springframework.biz.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

import org.springframework.web.context.support.WebApplicationContextUtils;

@SuppressWarnings("serial")
public class SpringAutowireServlet extends HttpServlet {

	public void init() throws ServletException {
		super.init();
		WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getAutowireCapableBeanFactory()
				.autowireBean(this);
	}

}
