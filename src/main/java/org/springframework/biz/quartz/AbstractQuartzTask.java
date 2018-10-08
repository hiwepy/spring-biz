package org.springframework.biz.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * 抽象的Quartz定时任务对象
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public abstract class AbstractQuartzTask implements InitializingBean{

	protected static Logger LOG = LoggerFactory.getLogger(AbstractQuartzTask.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			//调用抽象方法
			doAfterPropertiesSet();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}
	
	public abstract void doAfterPropertiesSet() throws Exception;
	
	
	public abstract void task();
	
	 
	
}