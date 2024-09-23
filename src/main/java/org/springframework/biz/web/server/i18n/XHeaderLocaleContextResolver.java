package org.springframework.biz.web.server.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;

public class XHeaderLocaleContextResolver implements LocaleContextResolver {

	/**
	 * 国际化（zh_CN：简体中文、zh_TW：繁体中文、en_US：英语）
	 */
	public static final String X_LANGUAGE = "X-Language";
	/**
	 * 客户端时区
	 */
	public static final String X_TIMEZONE = "X-TimeZone";
	
	private final List<Locale> supportedLocales = new ArrayList<>(4);

	@Nullable
	private Locale defaultLocale;

	@Nullable
	private TimeZone defaultTimeZone;

	/**
	 * Configure supported locales to check against the requested locales
	 * determined via {@link HttpHeaders#getAcceptLanguageAsLocales()}.
	 * @param locales the supported locales
	 */
	public void setSupportedLocales(List<Locale> locales) {
		this.supportedLocales.clear();
		this.supportedLocales.addAll(locales);
	}

	/**
	 * Return the configured list of supported locales.
	 * @return list of supported locales
	 */
	public List<Locale> getSupportedLocales() {
		return this.supportedLocales;
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
	 * This method may be overridden in subclasses.
	 * @return Default Locale
	 */
	@Nullable
	public Locale getDefaultLocale() {
		return this.defaultLocale;
	}

	/**
	 * Set a default TimeZone that this resolver will return if no other time zone found.
	 */
	public void setDefaultTimeZone(@Nullable TimeZone defaultTimeZone) {
		this.defaultTimeZone = defaultTimeZone;
	}
	
	/**
	 * Return the default TimeZone that this resolver is supposed to fall back to, if any.
	 * @return Default TimeZone
	 */
	@Nullable
	public TimeZone getDefaultTimeZone() {
		return this.defaultTimeZone;
	}
	
	@Override
	public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
		return new TimeZoneAwareLocaleContext() {
			
			@Override
			public Locale getLocale() {
				String language = exchange.getRequest().getHeaders().getFirst(X_LANGUAGE);
				Locale locale = StringUtils.hasText(language) ? Locale.forLanguageTag(language) : null;
				if (locale == null) {
					locale = determineDefaultLocale(exchange);
				}
				return locale;
			}
			
			@Override
			public TimeZone getTimeZone() {
				String timeZoneId = exchange.getRequest().getHeaders().getFirst(X_TIMEZONE);
				TimeZone timeZone = StringUtils.hasText(timeZoneId) ? TimeZone.getTimeZone(timeZoneId) : null;
				if (timeZone == null) {
					timeZone = determineDefaultTimeZone(exchange);
				}
				return timeZone;
			}
		};
		
	}

	@Override
	public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {
		if(localeContext != null) {
			LocaleContextHolder.setLocaleContext(localeContext);
		}
	}
	
	/**
	 * Determine the default locale for the given exchange,
	 * Called if no Locale session attribute has been found.
	 * <p>The default implementation returns the specified default locale,
	 * if any, else falls back to the request's accept-header locale.
	 * @param exchange the exchange to resolve the locale for
	 * @return the default locale (never {@code null})
	 * @see #setDefaultLocale
	 * @see jakarta.servlet.http.HttpServletRequest#getLocale()
	 */
	protected Locale determineDefaultLocale(ServerWebExchange exchange) {
		List<Locale> requestLocales = null;
		try {
			requestLocales = exchange.getRequest().getHeaders().getAcceptLanguageAsLocales();
		}
		catch (IllegalArgumentException ex) {
			// Invalid Accept-Language header: treat as empty for matching purposes
		}
		
		if (CollectionUtils.isEmpty(requestLocales)) {
			return this.defaultLocale;  // may be null
		}
		List<Locale> supportedLocales = getSupportedLocales();
		if (supportedLocales.isEmpty()) {
			return requestLocales.get(0);  // never null
		}

		Locale languageMatch = null;
		for (Locale locale : requestLocales) {
			if (supportedLocales.contains(locale)) {
				if (languageMatch == null || languageMatch.getLanguage().equals(locale.getLanguage())) {
					// Full match: language + country, possibly narrowed from earlier language-only match
					return locale;
				}
			}
			else if (languageMatch == null) {
				// Let's try to find a language-only match as a fallback
				for (Locale candidate : supportedLocales) {
					if (!StringUtils.hasLength(candidate.getCountry()) &&
							candidate.getLanguage().equals(locale.getLanguage())) {
						languageMatch = candidate;
						break;
					}
				}
			}
		}
		if (languageMatch != null) {
			return languageMatch;
		}

		return (this.defaultLocale != null ? this.defaultLocale : requestLocales.get(0));
	}

	/**
	 * Determine the default time zone for the given exchange,
	 * Called if no TimeZone session attribute has been found.
	 * <p>The default implementation returns the specified default time zone,
	 * if any, or {@code null} otherwise.
	 * @param exchange the exchange to resolve the time zone for
	 * @return the default time zone (or {@code null} if none defined)
	 * @see #setDefaultTimeZone
	 */
	@Nullable
	protected TimeZone determineDefaultTimeZone(ServerWebExchange exchange) {
		return getDefaultTimeZone();
	}

}