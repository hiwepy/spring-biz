package org.springframework.biz.web.servlet.i18n;

import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;

public class XHeaderLocaleResolver extends AbstractLocaleContextResolver {
	
	/**
	 * 国际化（zh_CN：简体中文、zh_TW：繁体中文、en_US：英语）
	 */
	public static final String X_LANGUAGE = "X-Language";
	/**
	 * 客户端时区
	 */
	public static final String X_TIMEZONE = "X-TimeZone";
	

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		String language = request.getHeader(X_LANGUAGE);
		Locale locale = StringUtils.hasText(language) ? Locale.forLanguageTag(language) : null;
		if (locale == null) {
			locale = determineDefaultLocale(request);
		}
		return locale;
	}
	
	@Override
	public LocaleContext resolveLocaleContext(final HttpServletRequest request) {
		return new TimeZoneAwareLocaleContext() {
			
			@Override
			public Locale getLocale() {
				String language = request.getHeader(X_LANGUAGE);
				Locale locale = StringUtils.hasText(language) ? Locale.forLanguageTag(language) : null;
				if (locale == null) {
					locale = determineDefaultLocale(request);
				}
				return locale;
			}
			
			@Override
			public TimeZone getTimeZone() {
				String timeZoneId = request.getHeader(X_TIMEZONE);
				TimeZone timeZone = StringUtils.hasText(timeZoneId) ? TimeZone.getTimeZone(timeZoneId) : null;
				if (timeZone == null) {
					timeZone = determineDefaultTimeZone(request);
				}
				return timeZone;
			}
		};
	}

	@Override
	public void setLocaleContext(HttpServletRequest request, @Nullable HttpServletResponse response,
			@Nullable LocaleContext localeContext) {
		if(localeContext != null) {
			LocaleContextHolder.setLocaleContext(localeContext);
		}
	}

	/**
	 * Determine the default locale for the given request,
	 * Called if no Locale session attribute has been found.
	 * <p>The default implementation returns the specified default locale,
	 * if any, else falls back to the request's accept-header locale.
	 * @param request the request to resolve the locale for
	 * @return the default locale (never {@code null})
	 * @see #setDefaultLocale
	 * @see javax.servlet.http.HttpServletRequest#getLocale()
	 */
	protected Locale determineDefaultLocale(HttpServletRequest request) {
		Locale defaultLocale = getDefaultLocale();
		if (defaultLocale == null) {
			defaultLocale = request.getLocale();
		}
		return defaultLocale;
	}

	/**
	 * Determine the default time zone for the given request,
	 * Called if no TimeZone session attribute has been found.
	 * <p>The default implementation returns the specified default time zone,
	 * if any, or {@code null} otherwise.
	 * @param request the request to resolve the time zone for
	 * @return the default time zone (or {@code null} if none defined)
	 * @see #setDefaultTimeZone
	 */
	@Nullable
	protected TimeZone determineDefaultTimeZone(HttpServletRequest request) {
		return getDefaultTimeZone();
	}

}
