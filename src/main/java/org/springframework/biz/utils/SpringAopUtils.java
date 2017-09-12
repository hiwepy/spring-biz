package org.springframework.biz.utils;

import java.lang.reflect.Field;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
/**
 * 
 * @className	： SpringAopUtils
 * @description	： 获取最终代理对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年4月18日 下午9:05:10
 * @version 	V1.0
 */
public class SpringAopUtils {

	public SpringAopUtils() {
	}

	public static Object getProxyTarget(Object proxiedInstance) {
		if (!(proxiedInstance instanceof Advised)) {
		} else {
			try {
				return getProxyTarget(((Advised) proxiedInstance).getTargetSource().getTarget());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return proxiedInstance;
	}
	
	/**
	 * 获取 目标对象
	 * 
	 * @param proxy 代理对象
	 * @return
	 * @throws Exception
	 */
	public static Object getTarget(Object proxy) throws Exception {
		// 不是代理对象
		if (!AopUtils.isAopProxy(proxy)) {
			return proxy;
		}
		if (AopUtils.isJdkDynamicProxy(proxy)) {
			return getJdkDynamicProxyTargetObject(proxy);
		} else { 
			// cglib
			return getCglibProxyTargetObject(proxy);
		}

	}

	private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
		Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
		h.setAccessible(true);
		Object dynamicAdvisedInterceptor = h.get(proxy);
		Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
		advised.setAccessible(true);
		Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
		return target;
	}

	private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
		Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
		h.setAccessible(true);
		AopProxy aopProxy = (AopProxy) h.get(proxy);
		Field advised = aopProxy.getClass().getDeclaredField("advised");
		advised.setAccessible(true);
		Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
		return target;
	}
}
