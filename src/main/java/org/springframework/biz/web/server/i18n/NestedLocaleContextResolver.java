package org.springframework.biz.web.server.i18n;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.List;

/**
 * 嵌套Locale解析器；解决同时设置Locale到Session和Cookie的问题
 */
public class NestedLocaleContextResolver implements LocaleContextResolver {
	
	protected List<LocaleContextResolver> resolvers;

	protected boolean isNested() {
		if(getResolvers() != null){
			return true;
		}
		return false;
	}
	
	public List<LocaleContextResolver> getResolvers() {
		return resolvers;
	}

	public void setResolvers(List<LocaleContextResolver> resolvers) {
		this.resolvers = resolvers;
	}

	@Override
	public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
		if(isNested()){
			for (LocaleContextResolver localeResolver : getResolvers()) {
				//解析locale
				LocaleContext localeContext = localeResolver.resolveLocaleContext(exchange);
				if(localeContext == null){
					continue;
				}
				LocaleContextHolder.setLocaleContext(localeContext);
				return localeContext;
			}
		}
		return null;
	}

	@Override
	public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {
		if(isNested()){
			for (LocaleContextResolver localeResolver : getResolvers()) {
				localeResolver.setLocaleContext(exchange, localeContext);
			}
		}
		
	}

}
