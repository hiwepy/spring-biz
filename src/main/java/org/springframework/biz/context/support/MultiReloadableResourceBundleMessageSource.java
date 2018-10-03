package org.springframework.biz.context.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

public class MultiReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {
	
	protected static Logger LOG = LoggerFactory.getLogger(MultiReloadableResourceBundleMessageSource.class);
	/** Pseudo URL prefix for loading from the class path: "classpath*:" */
	protected static final String CLASSPATH_URL_PREFIX = "classpath*:";
	protected static final String PROPERTIES_SUFFIX = ".properties";
	protected static final String XML_SUFFIX = ".xml";
	
	protected String[] basenames = new String[0];
	protected ResourceBasenameHandler basenameHandler = new ResourceBasenameHandler() {
		
		@Override
		public String handle(Resource resource) throws IOException {
			String filepath = resource.getFile().getAbsolutePath();
			return FilenameUtils.getFullPath(filepath) + FilenameUtils.getBaseName(filepath);
		}
	};
	/** Cache to hold Resource lists per filename */
	protected ConcurrentMap<String, List<Resource>> cachedResources = new ConcurrentHashMap<String, List<Resource>>();
	protected ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver();
	
	@Override
	public void setBasename(String basename) {
		setBasenames(basename);
	}
	
	@Override
	public void setBasenames(String... basenames) {
		if (basenames != null) {
			//解析资源basename
			List<String> basenameList = new ArrayList<String>();
			for(String basename : basenames){
				Assert.hasText(basename, "Basename must not be empty");
				//解析资源basename
				basenameList.addAll(calculateFilenamesForBasename(basename));
			}
			//对处理后的路径进行处理
			this.basenames = new String[basenameList.size()];
			for (int i = 0; i < basenameList.size(); i++) {
				this.basenames[i] = basenameList.get(i);
			}
		}
		else {
			this.basenames = new String[0];
		}
		super.setBasenames(this.basenames);
	}
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
		super.setResourceLoader(this.resourceLoader);
	}
	
	protected List<String> calculateFilenamesForBasename(String basename) {
		List<String> result = new ArrayList<String>(3);
		//解析Resource资源
		List<Resource> resourceList = calculateResourcesForBasename(basename);
		//对处理后的路径进行处理
		for (int i = 0; i < resourceList.size(); i++) {
			try {
				result.add(basenameHandler.handle(resourceList.get(i)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	protected List<Resource> calculateResourcesForBasename(String basename) {
		Assert.hasText(basename, "Basename must not be empty");
		List<Resource> resourceList = this.cachedResources.get(basename);
		if (resourceList != null) {
			return resourceList;
		}
		//解析Resource资源
		resourceList = new ArrayList<Resource>(7);
		//增加表达式路径支持
		if(resourceLoader instanceof ResourcePatternResolver){
			Resource[] resources = null;
			try {
				//spring 资源路径匹配解析器
				//“classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
				//“classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。带通配符的classpath使用“ClassLoader”的“Enumeration<URL> getResources(String name)”
				//方法来查找通配符之前的资源，然后通过模式匹配来获取匹配的资源。
				ResourcePatternResolver resourceResolver = (ResourcePatternResolver) resourceLoader;
				resources = resourceResolver.getResources(CLASSPATH_URL_PREFIX + basename + PROPERTIES_SUFFIX);
				if (resources == null || resources.length == 0 ) {
					resources = resourceResolver.getResources(CLASSPATH_URL_PREFIX + basename + XML_SUFFIX);
				}
			} catch (IOException e) {
				LOG.debug("No properties file found for [" + basename + "] - neither plain properties nor XML");
			}
			for(Resource resource : resources){
				if (resource.exists() || resource.isReadable()) {
					resourceList.add(resource);
				}
			}
		}else{
			Resource resource = this.resourceLoader.getResource(CLASSPATH_URL_PREFIX + basename + PROPERTIES_SUFFIX);
			if (!resource.exists()) {
				resource = this.resourceLoader.getResource(CLASSPATH_URL_PREFIX + basename + XML_SUFFIX);
			}
			if (resource.exists() || resource.isReadable()) {
				resourceList.add(resource);
			}
		}
		if (!resourceList.isEmpty()) {
			List<Resource> existing = this.cachedResources.putIfAbsent(basename, resourceList);
			if (existing != null) {
				resourceList = existing;
			}
		}
		return resourceList;
	}

	public ResourceBasenameHandler getBasenameHandler() {
		return basenameHandler;
	}

	public void setBasenameHandler(ResourceBasenameHandler basenameHandler) {
		this.basenameHandler = basenameHandler;
	} 
	
}
