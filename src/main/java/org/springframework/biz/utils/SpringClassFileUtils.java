package org.springframework.biz.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;


/**
 * Classpath 根目录资源文件加载工具
 */
public abstract class SpringClassFileUtils {

	//得到classpath 根目录
	public static File getClassRoot() throws FileNotFoundException{
		return SpringResourceUtils.getFile("classpath:");
	}
	
	public static File[] getClassPathFiles(String suffix) throws IOException {
		//得到classpath 根目录
		File classPath = SpringClassFileUtils.getClassRoot();
		//遍历根目录，仅仅取得指定后缀的文件
		Collection<File> files = FileUtils.listFiles(classPath, FileFilterUtils.suffixFileFilter(suffix), null);
		return files.toArray(new File[]{});
	}

}
