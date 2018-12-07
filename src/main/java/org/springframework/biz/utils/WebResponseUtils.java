package org.springframework.biz.utils;

import java.io.InputStream;
import java.util.Map;

import org.springframework.biz.web.servlet.view.HtmlResourceView;
import org.springframework.biz.web.servlet.view.StaticResourceView;
import org.springframework.biz.web.servlet.view.TextResourceView;
import org.springframework.biz.web.servlet.view.XMLResourceView;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xml.MappingJackson2XmlView;


public class WebResponseUtils {

	/**
	 * Returns true if the code is in [200..300), which means the request was
	 * successfully received, understood, and accepted.
	 */
	public static boolean isSuccessful(int statusCode) {
		return statusCode >= 200 && statusCode < 300;
	}

	public static ResponseEntity<InputStreamResource> responseEntity(HttpHeaders rtHeaders, long contentLength,
			String contentType, InputStream input) {
		MediaType parseMediaType = MediaType.parseMediaType(contentType);
		// 数据流
		return ResponseEntity.ok().headers(rtHeaders).contentLength(contentLength).contentType(parseMediaType)
				.body(new InputStreamResource(input));
	}
	
	public static ModelAndView htmlView(String content) {
		return htmlView(content, HttpStatus.OK);
	}

	public static ModelAndView htmlView(String content, HttpStatus status) {

		// 使用Html视图
		HtmlResourceView view = new HtmlResourceView(content);
		// 构造ModelAndView
		ModelAndView mav = new ModelAndView(view);
		mav.setStatus(status);

		return mav;
	}

	public static ModelAndView htmlView(String content, int statusCode) {
		return htmlView(content, HttpStatus.valueOf(statusCode));
	}

	public static ModelAndView jsonView(Map<String, ?> model) {
		return jsonView(model, MediaType.APPLICATION_JSON_VALUE, HttpStatus.OK);
	}
	
	public static ModelAndView jsonView(Map<String, ?> model, HttpStatus status ) {
		return jsonView(model, MediaType.APPLICATION_JSON_VALUE, status);
	}

	public static ModelAndView jsonView(Map<String, ?> model, String contentType, HttpStatus status ) {

		// 使用json视图
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		// 设置响应头
		jsonView.setContentType(contentType);
		// 构造ModelAndView
		ModelAndView mav = new ModelAndView(jsonView, model);
		mav.setStatus(status);

		return mav;
	}
	
	public static ModelAndView jsonView(String content) {
		return staticView(content, MediaType.APPLICATION_JSON_VALUE);
	}

	public static ModelAndView staticView(String content, String contentType) {

		// 使用Static视图
		StaticResourceView staticView = new StaticResourceView(content);
		// 设置响应头
		staticView.setContentType(contentType);
		// 构造ModelAndView
		ModelAndView mav = new ModelAndView(staticView);
		mav.setStatus(HttpStatus.OK);

		return mav;
	}

	public static ModelAndView staticView(String content, HttpStatus status, String contentType) {

		// 使用Static视图
		StaticResourceView staticView = new StaticResourceView(content);
		// 设置响应头
		staticView.setContentType(contentType);
		// 构造ModelAndView
		ModelAndView mav = new ModelAndView(staticView);
		mav.setStatus(status);

		return mav;
	}

	public static ModelAndView staticView(String content, int statusCode, String contentType) {
		return staticView(content, HttpStatus.valueOf(statusCode), contentType);
	}

	public static ModelAndView textView(String content) {
		return textView(content, HttpStatus.OK);
	}
	
	public static ModelAndView textView(String content, HttpStatus status) {

		// 使用Static视图
		TextResourceView view = new TextResourceView(content);
		// 构造ModelAndView
		ModelAndView mav = new ModelAndView(view);
		mav.setStatus(status);

		return mav;
	}

	public static ModelAndView textView(String content, int statusCode) {
		return textView(content, HttpStatus.valueOf(statusCode));
	}

	public static ModelAndView xmlView(Map<String, ?> model) {
		return xmlView(model, MediaType.APPLICATION_XML_VALUE);
	}
	
	public static ModelAndView xmlView(Map<String, ?> model, String contentType) {

		// 使用xml视图
		MappingJackson2XmlView xmlView = new MappingJackson2XmlView();
		// 设置响应头
		xmlView.setContentType(contentType);
		// 构造ModelAndView
		ModelAndView mav = new ModelAndView(xmlView, model);
		mav.setStatus(HttpStatus.OK);

		return mav;

	}
	
	public static ModelAndView xmlView(String content) {
		return xmlView(content, MediaType.APPLICATION_XML_VALUE, HttpStatus.OK);
	}
	
	public static ModelAndView xmlView(String content, HttpStatus status ) {
		return xmlView(content, MediaType.APPLICATION_XML_VALUE, status);
	}

	public static ModelAndView xmlView(String content, String contentType, HttpStatus status ) {

		// 使用xml视图
		XMLResourceView xmlView = new XMLResourceView(content);
		// 设置响应头
		xmlView.setContentType(contentType);
		// 构造ModelAndView
		ModelAndView mav = new ModelAndView(xmlView);
		mav.setStatus(status);

		return mav;
	}

}
