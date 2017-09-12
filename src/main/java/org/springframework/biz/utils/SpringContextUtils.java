package org.springframework.biz.utils;

import org.springframework.biz.context.SpringClassPathXmlInstanceContext;
import org.springframework.biz.context.SpringContext;

public class SpringContextUtils {

	private static SpringContext context = new SpringClassPathXmlInstanceContext(new String[0]);

	public SpringContextUtils() {
	}

	public static SpringContext getContext() {
		return SpringContextUtils.context;
	}

	public static void setContext(SpringContext context) {
		SpringContextUtils.context = context;
	}

}
