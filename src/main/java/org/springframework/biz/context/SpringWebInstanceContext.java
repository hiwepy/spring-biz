package org.springframework.biz.context;

import javax.servlet.ServletContext;

import org.springframework.biz.utils.SpringContextUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * 
 * @className	： SpringWebInstanceContext
 * @description	： WebApplicationContext上下文实例;根据ServletContext初始化的实例
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年4月18日 下午8:56:08
 * @version 	V1.0
 */
public class SpringWebInstanceContext extends AbstractSpringInstanceContext{

	public static void initialInstanceContext(ServletContext servletContext) {
		new SpringWebInstanceContext(WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext));
	}

	private SpringWebInstanceContext(ApplicationContext applicationContext) {
		super.setApplicationContext(applicationContext);
		SpringContextUtils.setContext(this);
		LOG.info("SpringWebInstanceContext initialization completed.");
	}
}
