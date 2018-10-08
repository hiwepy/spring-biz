package org.springframework.biz.aop;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.biz.aop.aware.BeanSelfAware;

public class InjectBeanSelfProcessor implements BeanPostProcessor {

	 public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {  
        if(bean instanceof BeanSelfAware){  
            BeanSelfAware myBean = (BeanSelfAware)bean;  
            myBean.setSelf(bean);  
            return myBean;  
        }  
        return bean;  
    }  
   
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {  
        return bean;  
    }  

}
