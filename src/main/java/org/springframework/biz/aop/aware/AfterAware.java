package org.springframework.biz.aop.aware;


/**
 * Aop 后置通知要调用的方法接口
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public abstract interface AfterAware {

	 public abstract void doAfter(Invocation invocation) throws Exception;
	 
}
