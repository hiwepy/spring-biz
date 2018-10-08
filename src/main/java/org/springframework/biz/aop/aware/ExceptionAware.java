package org.springframework.biz.aop.aware;

/**
 * Aop 异常通知要调用的方法接口
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public abstract interface ExceptionAware {
	
	 public abstract void doException(Invocation invocation)  throws Exception;
	 
}
