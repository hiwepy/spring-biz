package org.springframework.biz.web.servlet.view;

import java.io.ByteArrayInputStream;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.View;

/**
 * {@link View} backed by an HTML/Text resource.
 */
public class StaticResourceView implements View {

	private String content;
	private String contentType;
	
	public StaticResourceView(String content) {
		this.content = content;
	}
	
	public StaticResourceView(String content, String contentType) {
		this.content = content;
		this.contentType = contentType;
	}
	
	@Override
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType(getContentType());
		FileCopyUtils.copy(new ByteArrayInputStream(content.getBytes()),
				response.getOutputStream());
	}

	

}