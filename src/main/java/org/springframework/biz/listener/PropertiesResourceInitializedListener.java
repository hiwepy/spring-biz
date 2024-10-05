package org.springframework.biz.listener;

import hitool.core.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.biz.utils.SpringPropertiesUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 
 * 根据location 路径 获取 Properties并进行缓存
 * <p>注意：location 采用 spring 资源路径匹配解析器</p>
 * <ul>
 *    <li>1、“classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
 *    <li>2、“classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。
 *    <li>3、或单一路径，如："file:C:/test.dat"、"classpath:test.dat"、"WEB-INF/test.dat"
 * </ul>
 * 
 */
@Slf4j
public class PropertiesResourceInitializedListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		String location = context.getInitParameter("location");
		String encoding = context.getInitParameter("encoding");
		if(!StringUtils.isEmpty(location)){
			SpringPropertiesUtils.initProperties(location,StringUtils.getSafeStr(encoding,"utf-8"));
		}else{
			log.warn("location is null !");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
	
}
