package org.springframework.biz.aop.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @className	： Exceptional
 * @description	： Aop方法异常通知注解：有此注解的方法才会继续其他的操作
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年9月12日 下午11:26:54
 * @version 	V1.0
 */
@Target({ ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Exceptional {
	
	/**
	 * bean对象中要调用的方法
	 */
	public abstract String method() default "doException";

}