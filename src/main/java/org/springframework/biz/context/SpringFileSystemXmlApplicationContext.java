package org.springframework.biz.context;

import org.springframework.biz.utils.SpringContextUtils;
import org.springframework.context.support.FileSystemXmlApplicationContext;
/**
 * 
 * @className	： SpringFileSystemXmlApplicationContext
 * @description	： Spring 文件路径XML配置上下文实例
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年4月18日 下午8:56:32
 * @version 	V1.0
 */
public class SpringFileSystemXmlApplicationContext  extends AbstractSpringInstanceContext {

	public SpringFileSystemXmlApplicationContext(String[] locations) {
		if (locations.length < 1) {
			return;
		} else {
			super.setApplicationContext(new FileSystemXmlApplicationContext(locations));
			SpringContextUtils.setContext(this);
			LOG.info("SpringFileSystemXmlApplicationContext initialization completed.");
			return;
		}
	}
}
