package org.springframework.biz.web.servlet.i18n;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.util.Locale;

public class LocaleContextFilter extends OncePerRequestFilter {

	/** LocaleResolver used by this servlet. */
	@Nullable
	private LocaleResolver localeResolver;
	
	public LocaleContextFilter(LocaleResolver localeResolver) {
		super();
		this.localeResolver = localeResolver;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Locale locale = localeResolver.resolveLocale(request);
		LocaleContextHolder.setLocale(locale);
		filterChain.doFilter(request, response);
		//localeResolver.resolveLocaleContext(exchange)
		
	}
	
	
}