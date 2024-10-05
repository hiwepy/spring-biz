package org.springframework.biz.utils;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

public abstract class SpringResourceUtils extends org.springframework.util.ResourceUtils{

	//spring 资源路径匹配解析器
	//“classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
	//“classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。带通配符的classpath使用“ClassLoader”的“Enumeration<URL> getResources(String name)”
	//方法来查找通配符之前的资源，然后通过模式匹配来获取匹配的资源。
	protected static ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	
	/*
	 * “classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。
	 * 带通配符的classpath使用“ClassLoader”的“Enumeration<URL> getResources(String
	 * name)”方法来查找通配符之前的资源，
	 * 然后通过模式匹配来获取匹配的资源。如“classpath:META-INF/*.LIST”将首先加载通配符之前的目录
	 * “META-INF”，然后再遍历路径进行子路径匹配从而获取匹配的资源。
	 */
	public static String[] getRelativeResources(String location) throws IOException {
		String[] rs = SpringResourceUtils.getResourcePaths(location);
		String[] resouceArray = new String[rs.length];
		for (int i = 0; i < rs.length; i++) {
			String xdpath = rs[i].substring(rs[i].indexOf("classes") + 8);
			resouceArray[i] = xdpath;
		}
		return resouceArray;
	}

	public static String[] getResourcePaths(String location) throws IOException {
		String[] resouceArray = null;
		Resource[] resources = SpringResourceUtils.getResources(location);
		if (resources == null) {
			return new String[0];
		} else {
			resouceArray = new String[resources.length];
			for (int i = 0; i < resources.length; i++) {
				resouceArray[i] = resources[i].getURL().getPath();
				resouceArray[i] = resouceArray[i].substring(resouceArray[i].indexOf("classes") + 8);
			}
		}
		return resouceArray;
	}

	public static Resource getFileSystemResource(String location) {
		return new FileSystemResource(location);
	}

	public static Resource getResource(String location) {
		return resolver.getResource(location);
	}

	public static Resource[] getResources(String location) throws IOException {
		return resolver.getResources(location);
	}
	
	public static Resource[] getProperties() throws IOException {
		return SpringResourceUtils.getResources("classpath*:**/*.properties");
	}
	
	public static Resource[] getRootProperties() throws IOException {
		return SpringResourceUtils.getResources("classpath*:*.properties");
	}
	
	public static Resource[] getRuntimeProperties() throws IOException {
		return SpringResourceUtils.getResources("classpath*:**/runtime*.properties");
	}
	
	public static Resource[] getConfigProperties() throws IOException {
		return SpringResourceUtils.getResources("classpath*:conf/**/*.properties");
	}
	
	public static Resource[] getLogProperties() throws IOException {
		return SpringResourceUtils.getResources("classpath*:**/logger*.properties");
	}
	
	public static String[] getPropertiesPaths() throws IOException {
		return SpringResourceUtils.getResourcePaths("classpath*:**/*.properties");
	}
	
	public static String[] getRootPropertiesPaths() throws IOException {
		return SpringResourceUtils.getResourcePaths("classpath*:*.properties");
	}

	public static String[] getRuntimePropertiesPaths() throws IOException {
		return SpringResourceUtils.getResourcePaths("classpath*:**/runtime*.properties");
	}
	
	public static String[] getConfigPropertiesPaths() throws IOException {
		return SpringResourceUtils.getResourcePaths("classpath*:conf/**/*.properties");
	}
	
	public static String[] getLogPropertiesPaths() throws IOException {
		return SpringResourceUtils.getResourcePaths("classpath*:**/logger*.properties");
	}

	public static Resource[] getXMLs() throws IOException {
		return SpringResourceUtils.getResources("classpath*:**/*.xml");
	}
	
	public static Resource[] getSpringXMLs() throws IOException {
		return SpringResourceUtils.getResources("classpath*:config/spring/**/*.xml");
	}

	public static Resource[] getHibernateXMLs() throws IOException {
		return SpringResourceUtils.getResources("classpath*:config/hibernate/**/*.hbm.xml");
	}

	public static Resource[] getStruts1XMLs() throws IOException {
		return SpringResourceUtils.getResources("classpath*:config/struts1/**/*.xml");
	}

	public static Resource[] getStruts2XMLs() throws IOException {
		return SpringResourceUtils.getResources("classpath*:config/struts2/**/*.xml");
	}

	public static Resource[] getIbatisXMLs() throws IOException {
		return SpringResourceUtils.getResources("classpath*:config/ibatis/**/*.xml");
	}

	public static Resource[] getMybatisXMLs() throws IOException {
		return SpringResourceUtils.getResources("classpath*:config/mybatis/**/*.xml");
	}
	
	public static String[] getXMLPaths() throws IOException {
		return SpringResourceUtils.getResourcePaths("classpath*:**/*.xml");
	}
	
	public static String[] getSpringXMLPaths() throws IOException {
		return SpringResourceUtils.getResourcePaths("classpath*:config/spring/**/*.xml");
	}

	public static String[] getHibernateXMLPaths() throws IOException {
		return SpringResourceUtils.getResourcePaths("classpath*:config/hibernate/**/*.hbm.xml");
	}

	public static String[] getStruts1XMLPaths() throws IOException {
		return SpringResourceUtils.getResourcePaths("classpath*:config/struts1/**/*.xml");
	}

	public static String[] getStruts2XMLPaths() throws IOException {
		return SpringResourceUtils.getResourcePaths("classpath*:config/struts2/**/*.xml");
	}

	public static String[] getIbatisXMLPaths() throws IOException {
		return SpringResourceUtils.getResourcePaths("classpath*:config/ibatis/**/*.xml");
	}

	public static String[] getMybatisXMLPaths() throws IOException {
		return SpringResourceUtils.getResourcePaths("classpath*:config/mybatis/**/*.xml");
	}

	public static void main(String[] args) throws Exception {
		//String[] list = SpringResourceLoader.getInstance().getResourcePaths("classpath*:config/**/*.properties");
		String[] list = SpringResourceUtils.getLogPropertiesPaths();
		for (int i = 0; i < list.length; ++i) {
			System.out.println(list[i]);
		}
		
		//String[] list = ClasspathXMLResourceUtils.getResourcePaths("classpath*:config/**/*.properties");
		String[] list2 = SpringResourceUtils.getXMLPaths();
		for (int i = 0; i < list2.length; ++i) {
			System.out.println(list[i]);
		}
	}
	
}