package org.springframework.biz.aop.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @className	： Logger
 * @description	： Aop环绕通知注解：有此注解的class,方法上如果有Comment则会进行日志记录
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年9月12日 下午11:26:45
 * @version 	V1.0
 */
@Target({ ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Logger {
	
	/**
	 * spring 上下文中的Name
	 */
	public abstract String name() default "commonLogAspect";
	
	/**
	 * 功能模块代码
	 */
	public abstract String model() default "";
	
	/**
	 * 业务模块代码
	 */
	public abstract String business() default "";
	
	/**
	 * bean对象中要调用的方法
	 */
	public abstract String method() default "doProceeding";
	
}