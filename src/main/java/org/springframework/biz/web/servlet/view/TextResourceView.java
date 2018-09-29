package org.springframework.biz.web.servlet.view;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;

/**
 * {@link View} backed by an Text resource.
 */
public class TextResourceView extends StaticResourceView {

	public TextResourceView(String content) {
		super(content, MediaType.TEXT_PLAIN_VALUE);
	}

}