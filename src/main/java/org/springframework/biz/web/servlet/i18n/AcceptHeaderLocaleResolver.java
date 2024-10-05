package org.springframework.biz.web.servlet.i18n;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import java.util.Locale;

public class AcceptHeaderLocaleResolver extends AbstractLocaleResolver {

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		if (StringUtils.hasText(language)) {
			return Locale.forLanguageTag(language);
		}
		return getDefaultLocale();
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		if(locale != null) {
			LocaleContextHolder.setLocale(locale);
		}
	}

	
	
}
