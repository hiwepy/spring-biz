package org.springframework.biz.aop.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Aop方法后置通知注解：有此注解的方法才会继续其他的操作
 * @author <a href="https://github.com/vindell">vindell</a>
 */
@Target({ ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface After {
	
	/**
	 * bean对象中要调用的方法
	 */
	public abstract String method() default "doAfter";

}