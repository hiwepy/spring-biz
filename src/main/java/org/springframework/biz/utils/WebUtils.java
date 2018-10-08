package org.springframework.biz.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * http://blog.csdn.net/caoshuming_500/article/details/20952329
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public class WebUtils extends org.springframework.web.util.WebUtils {

	private static String[] headers = new String[]{"Cdn-Src-Ip", "X-Real-IP", "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
	private static String localIP = "127.0.0.1";       
	
	/**
	 * 获取请求客户端IP地址，支持代理服务器
	 * @param request {@link HttpServletRequest} 对象
	 * @return IP地址
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		
		// 1、获取客户端IP地址，支持代理服务器
		String remoteAddr = null;
		for (String header : headers) {
			remoteAddr = request.getHeader(header);
			if(StringUtils.hasText(remoteAddr) && !remoteAddr.equals("unknown")){
				break;
			}
		}
		// 2、没有取得特定标记的值
		if(!StringUtils.hasText(remoteAddr) ){
			remoteAddr = request.getRemoteAddr();
		}
		
		// 3、判断是否localhost访问
		if(remoteAddr.equals("localhost")){
			remoteAddr = localIP;
		}
		 
		return remoteAddr;
	}
	
	/**
	 *  获得请求的客户端信息【ip,port,name】
	 *  @return 客户端信息[ip,port,name]
	 */
	public static String[] getRemoteInfo(HttpServletRequest request) {
		if (request == null) {
			return new String[] { "", "", "" };
		}
		return new String[] { getRemoteAddr(request), request.getRemotePort() + "", request.getRemoteHost()};
	}
	
	
	 
	
}
