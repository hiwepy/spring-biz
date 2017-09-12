package org.springframework.biz.aop.aware;

public interface BeanSelfAware {
	 
	//实现BeanSelfAware接口 
	public void setSelf(Object proxyBean);
	
}
