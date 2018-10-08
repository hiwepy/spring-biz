package org.springframework.biz.aop.aware;


/**
 * Aop 前置通知要调用的方法接口
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public abstract interface BeforeAware {

	public abstract void doBefore(Invocation invocation)  throws Exception;
	
}
