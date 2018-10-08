package org.springframework.biz.web.servlet.i18n;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.biz.utils.LocaleUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

/**
 * 嵌套Locale解析器；解决同时设置Locale到Session和Cookie的问题
 */
public class NestedLocaleResolver extends AbstractLocaleResolver implements LocaleResolver {
	
	protected List<LocaleResolver> resolvers;
	
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		if(isNested()){
			Locale def = getDefaultLocale();
			for (LocaleResolver localeResolver : getResolvers()) {
				//解析locale
				Locale locale = localeResolver.resolveLocale(request);
				if(locale == null || locale.equals(def)){
					continue;
				}
				return locale;
			}
		}
		return getDefaultLocale();
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		if(isNested()){
			for (LocaleResolver localeResolver : getResolvers()) {
				localeResolver.setLocale(request, response, locale);
				LocaleUtils.setLocale(locale);
			}
		}
	}

	protected boolean isNested() {
		if(getResolvers() != null){
			return true;
		}
		return false;
	}
	
	public List<LocaleResolver> getResolvers() {
		return resolvers;
	}

	public void setResolvers(List<LocaleResolver> resolvers) {
		this.resolvers = resolvers;
	}

}
