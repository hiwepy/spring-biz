package org.springframework.biz.web.servlet.theme;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.theme.AbstractThemeResolver;

/**
 * 嵌套Theme解析器；解决同时设置Theme到Session和Cookie的问题
 */
public class NestedThemeResolver extends AbstractThemeResolver implements ThemeResolver {

	protected List<ThemeResolver> resolvers;

	@Override
	public String resolveThemeName(HttpServletRequest request) {
		if (isNested()) {
			String def = getDefaultThemeName();
			for (ThemeResolver localeResolver : getResolvers()) {
				// 解析theme
				String theme = localeResolver.resolveThemeName(request);
				if (theme == null || theme.equals(def)) {
					continue;
				}
				return theme;
			}
		}
		return getDefaultThemeName();
	}

	@Override
	public void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName) {
		if (isNested()) {
			for (ThemeResolver localeResolver : getResolvers()) {
				localeResolver.setThemeName(request, response, themeName);
			}
		}
	}

	protected boolean isNested() {
		if (getResolvers() != null) {
			return true;
		}
		return false;
	}

	public List<ThemeResolver> getResolvers() {
		return resolvers;
	}

	public void setResolvers(List<ThemeResolver> resolvers) {
		this.resolvers = resolvers;
	}

}
