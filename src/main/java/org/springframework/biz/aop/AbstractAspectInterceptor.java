package org.springframework.biz.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.biz.aop.aware.AfterAware;
import org.springframework.biz.aop.aware.BeforeAware;
import org.springframework.biz.aop.aware.ExceptionAware;
import org.springframework.biz.aop.aware.Invocation;
import org.springframework.biz.utils.AspectUtils;
import org.springframework.biz.factory.EnhancedBeanFactory;

/**
 * 基于Spring AOP 的方法切面拦截器
 */
public abstract class AbstractAspectInterceptor extends EnhancedBeanFactory {
	
	protected Logger LOG = LoggerFactory.getLogger(getClass());
	
	public void before(JoinPoint point) throws Throwable {
		String mathodName = point.getSignature().getName();
		if("setSelf".equalsIgnoreCase(mathodName)){
    		return;
    	}
		Object target = point.getTarget();
		Object[] args = point.getArgs();
		Method method = AspectUtils.getMethod(point);
    	//如果实现了BeforeAware
		if(method.getDeclaringClass().isInstance(BeforeAware.class)){
			//获得前置通知调用方法doBefore
			Method doBefore = BeanUtils.findMethod(method.getDeclaringClass(),"doBefore",Invocation.class);
			//执行doBefore
			if(doBefore!=null){
				doBefore.invoke(target,new Object[]{new Invocation(target, method, args)});
			}
		}else{
			//调用接口
			this.doBefore(point);
		}
	}
	
	public abstract void doBefore(JoinPoint point) throws Throwable;
	
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {  
    	try {
    		Object result = joinPoint.proceed();
    		String mathodName = joinPoint.getSignature().getName();
    		if("setSelf".equalsIgnoreCase(mathodName)){
        		return result;
        	}
    		return this.doAround(joinPoint);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.warn("invoke(ProceedingJoinPoint) - exception ignored", e); //$NON-NLS-1$ 
        }finally {
        	if (LOG.isDebugEnabled()) {
            	LOG.debug("invoke(ProceedingJoinPoint) - end"); //$NON-NLS-1$
            }
        }
        return null; 
    }  
	
	public abstract Object doAround(ProceedingJoinPoint joinPoint) throws Throwable;
	
    public void afterReturning(JoinPoint point,Object returnValue) throws Throwable {  
    	String mathodName = point.getSignature().getName();
		if("setSelf".equalsIgnoreCase(mathodName)){
    		return;
    	}
		Object target = point.getTarget();
		Object[] args = point.getArgs();
		Method method = AspectUtils.getMethod(point);
		//如果实现了AfterAware
    	if(method.getDeclaringClass().isInstance(AfterAware.class)){
    		//获得后置通知调用方法doAfter
    		Method doAfter = BeanUtils.findMethod(method.getDeclaringClass(), "doAfter",Invocation.class);
    		//执行doAfter
    		if(doAfter!=null){
    			doAfter.invoke(target,new Object[]{new Invocation(target, method, args, returnValue)});
    		}
    	}else{
    		this.doAfterReturning(point,returnValue);
    	}
    }  
	
    public abstract void doAfterReturning(JoinPoint point,Object returnValue) throws Throwable;
    
    public void afterThrowing(JoinPoint point,Throwable ex) throws Throwable {  
    	
    	String mathodName = point.getSignature().getName();
		if("setSelf".equalsIgnoreCase(mathodName)){
    		return;
    	}
		Object target = point.getTarget();
		Object[] args = point.getArgs();
		Method method = AspectUtils.getMethod(point);
		//如果实现了ExceptionAware
    	if(method.getDeclaringClass().isInstance(ExceptionAware.class)){
    		//获得异常通知调用方法doException
    		Method doException = BeanUtils.findMethod(method.getDeclaringClass(), "doException",Invocation.class);
    		//执行doException
    		if(doException != null){
    			doException.invoke(target,new Object[]{new Invocation(target, method, args, ex)});
    		}
    	}else{
    		this.doAfterThrowing(point, ex);
    	}
    }
    
    public abstract void doAfterThrowing(JoinPoint point,Throwable ex) throws Throwable;
	
}
