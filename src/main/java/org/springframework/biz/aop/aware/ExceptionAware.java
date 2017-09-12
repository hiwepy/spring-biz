package org.springframework.biz.aop.aware;

/**
 * 
 * @className	： ExceptionAware
 * @description	： Aop 异常通知要调用的方法接口
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年9月12日 下午11:25:45
 * @version 	V1.0
 */
public abstract interface ExceptionAware {
	
	/**
	 * 
	 * @description	： Aop 异常通知调用的方法
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
	 * @date 		：2017年9月12日 下午11:25:38
	 * @param invocation
	 * @throws Exception
	 */
	 public abstract void doException(Invocation invocation)  throws Exception;
	 
}
