package org.springframework.biz.aop.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @className	： DynamicDataSource
 * @description	： 动态数据源切换注解：有此注解的dao方法会自动切换数据源
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年9月12日 下午11:27:05
 * @version 	V1.0
 */
@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DynamicDataSource {

	/**
	 * 数据源的Name
	 */
	public abstract String dataSource() default "";
	
}
