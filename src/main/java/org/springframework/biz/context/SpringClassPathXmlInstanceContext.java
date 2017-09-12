package org.springframework.biz.context;

import org.springframework.biz.utils.SpringContextUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @className	： SpringClassPathXmlInstanceContext
 * @description	： Spring 类路径XML配置上下文实例
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年4月18日 下午8:58:45
 * @version 	V1.0
 */
public class SpringClassPathXmlInstanceContext extends AbstractSpringInstanceContext {

	public SpringClassPathXmlInstanceContext(String[] locations) {
		if (locations.length < 1) {
			return;
		} else {
			super.setApplicationContext(new ClassPathXmlApplicationContext(locations));
			SpringContextUtils.setContext(this);
			LOG.info("SpringClassPathXmlInstanceContext initialization completed.");
			return;
		}
	}
}
