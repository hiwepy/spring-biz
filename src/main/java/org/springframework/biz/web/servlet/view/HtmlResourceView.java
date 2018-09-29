package org.springframework.biz.web.servlet.view;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;

/**
 * {@link View} backed by an HTML resource.
 */
public class HtmlResourceView extends StaticResourceView {
	
	public HtmlResourceView(String content) {
		super(content, MediaType.TEXT_HTML_VALUE);
	}

}