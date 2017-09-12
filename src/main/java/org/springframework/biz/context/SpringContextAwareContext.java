package org.springframework.biz.context;

import org.springframework.beans.BeansException;
import org.springframework.biz.utils.SpringContextUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @className	： SpringContextAwareContext
 * @description	： ApplicationContext 注入型上下文实例（用于取spring容器中定义的bean）
 * <b>Example:</b>
 * <pre>
 * 配置：
 * &lt;bean id="springAwareContext" class="org.springframework.biz.context.SpringContextAwareContext"/&gt;
 * </pre>
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年4月18日 下午8:57:06
 * @version 	V1.0
 */
public class SpringContextAwareContext extends AbstractSpringInstanceContext implements ApplicationContextAware{
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		super.setApplicationContext(applicationContext);
		SpringContextUtils.setContext(this);
		LOG.info("SpringContextAwareContext initialization completed.");
	}

}
