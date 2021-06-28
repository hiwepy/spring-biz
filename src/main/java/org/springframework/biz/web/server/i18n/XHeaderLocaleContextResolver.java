package org.springframework.biz.web.server.i18n;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;

public class XHeaderLocaleContextResolver implements LocaleContextResolver {

	public static final String X_LANGUAGE = "X-Language";
	
	@Nullable
	private Locale defaultLocale = Locale.SIMPLIFIED_CHINESE;
	
	@Override
	public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
		String language = exchange.getRequest().getHeaders().getFirst(X_LANGUAGE);
		Locale targetLocale = defaultLocale;
		if (StringUtils.hasText(language)) {
			targetLocale = Locale.forLanguageTag(language);
		}
		return new SimpleLocaleContext(targetLocale);
	}

	@Override
	public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {
		if(localeContext != null) {
			LocaleContextHolder.setLocaleContext(localeContext);
		}
	}
	
	/**
	 * Configure a fixed default locale to fall back on if the request does not
	 * have an "Accept-Language" header (not set by default).
	 * @param defaultLocale the default locale to use
	 */
	public void setDefaultLocale(@Nullable Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	/*
	 * The configured default locale, if any.
	 * <p>This method may be overridden in subclasses.
	 */
	@Nullable
	public Locale getDefaultLocale() {
		return this.defaultLocale;
	}

}