package org.springframework.biz.aop.aware;


/**
 * 
 * @className	： AfterAware
 * @description	： Aop 后置通知要调用的方法接口
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年9月12日 下午11:26:27
 * @version 	V1.0
 */
public abstract interface AfterAware {

	/**
	 * 
	 * @description	： Aop 后置通知调用的方法
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
	 * @date 		：2017年9月12日 下午11:26:20
	 * @param invocation
	 * @throws Exception
	 */
	 public abstract void doAfter(Invocation invocation) throws Exception;
	 
}
