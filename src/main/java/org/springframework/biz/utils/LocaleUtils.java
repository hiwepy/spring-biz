package org.springframework.biz.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.util.Locale;

/**
 * @author <a href="https://github.com/hiwepy">hiwepy</a>
 */
public class LocaleUtils extends org.apache.commons.lang3.LocaleUtils {

	public final static String SESSION_KEY = "WW_TRANS_I18N_LOCALE";
	public final static String DEFAULT_LANGUAGE = "zh_CN";
	public final static Locale DEFAULT_LOCALE = Locale.SIMPLIFIED_CHINESE;
	public final static String STATCK_KEY = "language";

	private static ThreadLocal<Locale> threadLocale = new ThreadLocal<Locale>() {

		protected Locale initialValue() {
			return Locale.SIMPLIFIED_CHINESE;
		};

	};
	
	public static Locale getLocale() {
		return threadLocale.get();
	}
	
	public static void setLocale(Locale locale) {
		threadLocale.set(locale);
	}
	
	public static Locale getLocale(HttpServletRequest request) {
		// 会话作废前取出,原Locale
		Locale locale = LocaleUtils.getSessionLocale(request.getSession());
		if (locale == null) {
			return DEFAULT_LOCALE;
		}
		return locale;
	}

	public static String getLocaleKey(HttpServletRequest request) {
		// 会话作废前取出,原Locale
		Locale locale = LocaleUtils.getSessionLocale(request.getSession());
		if (locale == null) {
			locale = LocaleUtils.getRequestLocale(request);
		}
		if (locale == null) {
			return DEFAULT_LANGUAGE;
		}
		return locale.getLanguage() + "_" + locale.getCountry();
	}

	public static String getLocalePath(HttpServletRequest request, String filepath) {
		return LocaleUtils.getLocalePath(LocaleUtils.getLocaleKey(request), filepath);
	}

	public static String getLocalePath(String locale, String filepath) {
		String finalpath = filepath;
		if (locale != null) {
			String fullPath = FilenameUtils.getFullPath(filepath);
			String baseName = FilenameUtils.getBaseName(filepath);
			String extension = FilenameUtils.getExtension(filepath);
			finalpath = fullPath + baseName + "_" + locale + "." + extension;
		}
		File file = new File(finalpath);
		if (!file.exists()) {
			finalpath = filepath;
		}
		return finalpath;
	}

	public static Locale getRequestLocale(HttpServletRequest request) {
		String language = request.getParameter("language");
		if (language == null) {
			language = DEFAULT_LANGUAGE;
		}
		String loc[] = language.split("_");
		return new Locale(loc[0], loc[1]);
	}

	public static Locale getSessionLocale(HttpSession session) {
		// 会话作废前取出,原Locale
		return (Locale) session.getAttribute(SESSION_KEY);
	}

	public static Locale interceptLocale(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Locale locale = LocaleUtils.getSessionLocale(session);
		if (locale == null) {
			locale = LocaleUtils.getRequestLocale(request);
		}
		LocaleUtils.setSessionLocale(session, locale);
		return locale;
	}

	public static Locale interceptLocaleWithSessionInvalid(HttpServletRequest request) {
		// 作废前的Session
		HttpSession session = request.getSession();
		// 取出Locale
		Locale locale = LocaleUtils.getSessionLocale(request.getSession());
		if (locale == null) {
			locale = LocaleUtils.getRequestLocale(request);
		}
		// 作废Session
		session.invalidate();
		// 设置原Locale到新的Session
		LocaleUtils.setSessionLocale(request.getSession(), locale);
		return locale;
	}

	public static void setSessionLocale(HttpSession session) {
		// 会话作废前取出,原Locale
		Locale locale = (Locale) session.getAttribute(SESSION_KEY);
		if (locale == null) {
			locale = LocaleUtils.toLocale(DEFAULT_LANGUAGE);
		}
		session.setAttribute(SESSION_KEY, locale);
	}

	public static void setSessionLocale(HttpSession session, Locale locale) {
		if (locale == null) {
			locale = LocaleUtils.toLocale(DEFAULT_LANGUAGE);
		}
		session.setAttribute(SESSION_KEY, locale);
	}

}
