package org.springframework.biz.aop.aware;


/**
 * 
 * @className	： BeforeAware
 * @description	： Aop 前置通知要调用的方法接口
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年9月12日 下午11:26:07
 * @version 	V1.0
 */
public abstract interface BeforeAware {

	/**
	 * 
	 * @description	： Aop 前置通知调用的方法
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
	 * @date 		：2017年9月12日 下午11:25:59
	 * @param invocation
	 * @throws Exception
	 */
	public abstract void doBefore(Invocation invocation)  throws Exception;
	
}
