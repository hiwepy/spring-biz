package org.springframework.biz.web.multipart.cos;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

/**
 * {
 * 
 * @link MultipartResolver } implementation for Jason Hunter's
 *       <a href="http://www.servlets.com/cos">COS (com.oreilly.servlet)</a>.
 *       Works with a COS MultipartRequest underneath.
 * 
 *       <p>
 *       Provides "maxUploadSize" and "defaultEncoding" settings as bean
 *       properties; see respective MultipartRequest constructor parameters for
 *       details. Default maximum file size is unlimited; fallback encoding is
 *       the platform's default.
 * 
 * @author Juergen Hoeller
 * @since 06.10.2003
 * @see CosMultipartHttpServletRequest
 * @see com.CosMultipartRequest.servlet.MultipartRequest
 */
public class CosMultipartResolver extends CosFileUploadSupport implements MultipartResolver, ServletContextAware {

	private boolean resolveLazily = false;

	/**
	 * Constructor for use as bean. Determines the servlet container's temporary
	 * directory via the ServletContext passed in as through the
	 * ServletContextAware interface (typically by a WebApplicationContext).
	 * 
	 * @see #setServletContext
	 * @see org.springframework.web.context.ServletContextAware
	 * @see org.springframework.web.context.WebApplicationContext
	 */
	public CosMultipartResolver() {
		super();
	}

	/**
	 * Constructor for standalone usage. Determines the servlet container's
	 * temporary directory via the given ServletContext.
	 * 
	 * @param servletContext
	 *            the ServletContext to use (must not be <code>null</code>)
	 * @throws IllegalArgumentException
	 *             if the supplied {
	 * @link ServletContext } is <code>null</code>
	 */
	public CosMultipartResolver(ServletContext servletContext) {
		this();
		this.setServletContext(servletContext);
	}

	/**
	 * Set whether to resolve the multipart request lazily at the time of
	 * file or parameter access.
	 * <p>Default is "false", resolving the multipart elements immediately, throwing
	 * corresponding exceptions at the time of the {@link #resolveMultipart} call.
	 * Switch this to "true" for lazy multipart parsing, throwing parse exceptions
	 * once the application attempts to obtain multipart files or parameters.
	 */
	public void setResolveLazily(boolean resolveLazily) {
		this.resolveLazily = resolveLazily;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		if (!isUploadTempDirSpecified()) {
			try {
				super.setUploadTempDir(WebUtils.getTempDir(servletContext));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isMultipart(HttpServletRequest request) {
		return request.getContentType() != null && request.getContentType().startsWith(MULTIPART_CONTENT_TYPE);
	}
	
	@Override
	public MultipartHttpServletRequest resolveMultipart(final HttpServletRequest request) throws MultipartException {
		Assert.notNull(request, "Request must not be null");
		if (this.resolveLazily) {
			return new DefaultMultipartHttpServletRequest(request) {
				@Override
				protected void initializeMultipart() {
					MultipartParsingResult parsingResult = parseRequest(request);
					setMultipartFiles(parsingResult.getMultipartFiles());
					setMultipartParameters(parsingResult.getMultipartParameters());
					setMultipartParameterContentTypes(parsingResult.getMultipartParameterContentTypes());
				}
			};
		}
		else {
			MultipartParsingResult parsingResult = parseRequest(request);
			return new DefaultMultipartHttpServletRequest(request, parsingResult.getMultipartFiles(),
					parsingResult.getMultipartParameters(), parsingResult.getMultipartParameterContentTypes());
		}
	}
	
	/**
	 * Parse the given servlet request, resolving its multipart elements.
	 * @param request the request to parse
	 * @return the parsing result
	 * @throws MultipartException if multipart resolution failed.
	 */
	protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
		try {
			CosMultipartRequest multipartRequest = newMultipartRequest(request);
			multipartRequest.setPreserveFilename(preserveFilename);
			return parseFileItems(multipartRequest);
		} catch (IOException ex) {

			// Unfortunately, COS always throws an IOException,
			// so we need to check the error message here!
			if (ex.getMessage().indexOf("exceeds limit") != -1) {
				throw new MaxUploadSizeExceededException(getMaxUploadSize(), ex);
			} else {
				throw new MultipartException("Could not parse multipart request", ex);
			}

		}
	}

	/**
	 * Create a com.oreilly.servlet.MultipartRequest for the given HTTP request.
	 * Can be overridden to use a custom subclass, e.g. for testing purposes.
	 * 
	 * @param request current HTTP request
	 * @return the new MultipartRequest
	 * @throws IOException if thrown by the MultipartRequest constructor
	 */
	protected CosMultipartRequest newMultipartRequest(HttpServletRequest request) throws IOException {
		String encoding = determineEncoding(request);
		String tempPath = getUploadTempDir().getAbsolutePath();
		return new CosMultipartRequest(request, tempPath, getMaxUploadSize(), encoding);

	}

	/**
	 * Determine the encoding for the given request. Can be overridden in
	 * subclasses.
	 * <p>
	 * The default implementation checks the request encoding, falling back to
	 * the default encoding specified for this resolver.
	 * 
	 * @param request current HTTP request
	 * @return the encoding for the request (never <code>null</code>)
	 * @see javax.servlet.ServletRequest#getCharacterEncoding
	 * @see #setDefaultEncoding
	 */
	protected String determineEncoding(HttpServletRequest request) {
		String enc = request.getCharacterEncoding();
		if (enc == null) {
			enc = getDefaultEncoding();
		}
		return enc;
	}

	@Override
	public void cleanupMultipart(MultipartHttpServletRequest request) {
		if (request != null) {
			try {
				cleanupFileItems(request.getMultiFileMap());
			}
			catch (Throwable ex) {
				logger.warn("Failed to perform multipart cleanup for servlet request", ex);
			}
		}
	}

}
