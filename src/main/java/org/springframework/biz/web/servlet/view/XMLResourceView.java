package org.springframework.biz.web.servlet.view;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;

/**
 * {@link View} backed by an Text resource.
 */
public class XMLResourceView extends StaticResourceView {

	public XMLResourceView(String content) {
		super(content, MediaType.APPLICATION_XML_VALUE);
	}

}