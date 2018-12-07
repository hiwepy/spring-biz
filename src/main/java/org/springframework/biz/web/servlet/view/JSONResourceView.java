package org.springframework.biz.web.servlet.view;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;

/**
 * {@link View} backed by an Json resource.
 */
public class JSONResourceView extends StaticResourceView {

	public JSONResourceView(String content) {
		super(content, MediaType.APPLICATION_JSON_UTF8_VALUE);
	}

}