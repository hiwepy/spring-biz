package org.springframework.biz.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.biz.context.SpringWebInstanceContext;

/**
 * Spring 自定义上下文初始化监听
 */
public class SpringContextInitializedListener implements ServletContextListener {

	protected static Logger LOG = LoggerFactory.getLogger(SpringContextInitializedListener.class);
	
	public void contextInitialized(ServletContextEvent event) {
		LOG.info(" SpringContext initialize start ... ");
		SpringWebInstanceContext.initialInstanceContext(event.getServletContext());
		LOG.info("SpringContext initialized.");
	}

	public void contextDestroyed(ServletContextEvent event) {
		LOG.info("SpringContext destroyed .");
	}
	
}
