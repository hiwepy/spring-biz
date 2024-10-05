package org.springframework.biz.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.biz.context.SpringWebInstanceContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Spring 自定义上下文初始化监听
 */
@Slf4j
public class SpringContextInitializedListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.info(" SpringContext initialize start ... ");
		SpringWebInstanceContext.initialInstanceContext(event.getServletContext());
		log.info("SpringContext initialized.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		log.info("SpringContext destroyed .");
	}
	
}
