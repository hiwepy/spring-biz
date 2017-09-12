package org.springframework.biz.context;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public  class AbstractSpringInstanceContext implements SpringContext {
	
	protected static Logger LOG = LoggerFactory.getLogger(AbstractSpringInstanceContext.class);

	private ApplicationContext applicationContext;

	public AbstractSpringInstanceContext() {}
	
	@Override
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public <T> T getInstance(Class<T> requiredType) {
		//获得首字母小写的类名称如：ApplicationContext 变为 applicationContext
		String simpleName = StringUtils.uncapitalize(requiredType.getSimpleName());
		//或bean
		T bean = getApplicationContext().getBean(simpleName,requiredType);
		//没有获得继续处理
		if(bean==null){
			//获得所有匹配bean类型的名称
			String beanNames[] = getApplicationContext().getBeanNamesForType(requiredType);
			if (beanNames.length == 0){
				return getApplicationContext().getBean(requiredType);
			}else{
				return getApplicationContext().getBean(beanNames[0],requiredType);
			}
		}else{
			return bean;
		}
	}
	
	@Override
	public Object getInstance(String beanName) {
		return getApplicationContext().getBean(beanName);
	}
	
	@Override
	public DataSource getDataSource(){
		return (DataSource) this.getInstance("dataSource");
	}
	
	@Override
	public DataSource getDataSource(String datasourceName){
		return (DataSource) this.getInstance(datasourceName);
	}
	
}
