package org.springframework.biz.web.multipart.cos;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

public class CosFileUploadSupport {

	/** Logger available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Constant identifier for the mulipart content type : 'multipart/form-data'.
	 */
	protected static final String MULTIPART_CONTENT_TYPE = "multipart/form-data";
	
	protected boolean uploadTempDirSpecified = false;

	protected boolean preserveFilename = false;

	protected int maxUploadSize = Integer.MAX_VALUE;

	protected String defaultEncoding = WebUtils.DEFAULT_CHARACTER_ENCODING;

	protected File uploadTempDir;
	
	/**
	 * Set the maximum allowed size (in bytes) before uploads are refused. -1
	 * indicates no limit (the default).
	 * 
	 * @param maxUploadSize the maximum file size allowed
	 */
	public void setMaxUploadSize(int maxUploadSize) {

		this.maxUploadSize = maxUploadSize;

	}

	/**
	 * Return the maximum allowed size (in bytes) before uploads are refused.
	 * @return the maximum allowed size (in bytes)
	 */
	protected int getMaxUploadSize() {

		return maxUploadSize;

	}

	/**
	 * <p>
	 * Set the default character encoding to use for parsing requests, to be
	 * applied to headers of individual parts and to form fields. Default is
	 * ISO-8859-1, according to the Servlet spec.
	 * </p>
	 * <p>
	 * If the request specifies a character encoding itself, the request
	 * encoding will override this setting. This also allows for generically
	 * overriding the character encoding in a filter that invokes the
	 * ServletRequest.setCharacterEncoding method.
	 * </p>
	 * @param defaultEncoding the character encoding to use
	 * @see javax.servlet.ServletRequest#getCharacterEncoding
	 * @see javax.servlet.ServletRequest#setCharacterEncoding
	 * @see WebUtils#DEFAULT_CHARACTER_ENCODING
	 */
	public void setDefaultEncoding(String defaultEncoding) {

		this.defaultEncoding = defaultEncoding;

	}

	/**
	 * Return the default character encoding to use for parsing requests.
	 */
	protected String getDefaultEncoding() {
		return defaultEncoding;

	}

	/**
	 * Set the temporary directory where uploaded files get stored. Default is
	 * the servlet container's temporary directory for the web application.
	 * @param uploadTempDir upload tempdir
	 * @throws IOException  in case of general resolution/reading failures
	 * @see org.springframework.web.util.WebUtils#TEMP_DIR_CONTEXT_ATTRIBUTE
	 */
	public void setUploadTempDir(Resource uploadTempDir) throws IOException {
		if (!uploadTempDir.exists() && !uploadTempDir.getFile().mkdirs()) {
			throw new IllegalArgumentException("Given uploadTempDir [" + uploadTempDir + "] could not be created");
		}
		this.uploadTempDir = uploadTempDir.getFile();
	}

	public void setUploadTempDir(File uploadTempDir) throws IOException {
		this.uploadTempDir = uploadTempDir;
	}
	
	/**
	 * Return the temporary directory where uploaded files get stored.
	 * @return upload tempdir
	 */
	protected File getUploadTempDir() {
		return uploadTempDir;
	}
	
	protected boolean isUploadTempDirSpecified() {
		return this.uploadTempDirSpecified;
	}

	/**
	 * Set whether to preserve the filename as sent by the client, not stripping off
	 * path information in {@link CosMultipartFile#getOriginalFilename()}.
	 * <p>Default is "false", stripping off path information that may prefix the
	 * actual filename e.g. from Opera. Switch this to "true" for preserving the
	 * client-specified filename as-is, including potential path separators.
	 * @param preserveFilename whither preserveFilename
	 * @since 4.3.5
	 * @see MultipartFile#getOriginalFilename()
	 * @see CosMultipartFile#setPreserveFilename(boolean)
	 */
	public void setPreserveFilename(boolean preserveFilename) {
		this.preserveFilename = preserveFilename;
	}
	
	/**
	 * Parse the given List of Commons FileItems into a Spring MultipartParsingResult,
	 * containing Spring MultipartFile instances and a Map of multipart parameter.
	 * @param multipartRequest the multipartRequest to parse
	 * @return the Spring MultipartParsingResult
	 */
	protected MultipartParsingResult parseFileItems(CosMultipartRequest multipartRequest) {
		
		MultiValueMap<String, MultipartFile> multipartFiles = multipartRequest.getMultiFileMap();
		Map<String, String[]> multipartParameters = multipartRequest.getMultipartParameters();
		Map<String, String> multipartParameterContentTypes = multipartRequest.getMultipartParameterContentTypes();
		
		return new MultipartParsingResult(multipartFiles, multipartParameters, multipartParameterContentTypes);
	}

	/**
	 * Cleanup the Spring MultipartFiles created during multipart parsing,
	 * potentially holding temporary data on disk.
	 * <p>Deletes the underlying Commons FileItem instances.
	 * @param multipartFiles Collection of MultipartFile instances
	 * @see org.apache.commons.fileupload.FileItem#delete()
	 */
	protected void cleanupFileItems(MultiValueMap<String, MultipartFile> multipartFiles) {
		for (List<MultipartFile> files : multipartFiles.values()) {
			for (MultipartFile file : files) {
				if (file instanceof CosMultipartFile) {
					CosMultipartFile cmf = (CosMultipartFile) file;
					cmf.getFile().delete();
					if (logger.isDebugEnabled()) {
						logger.debug("Cleaning up multipart file [" + cmf.getName() + "] with original filename [" +
								cmf.getOriginalFilename() + "], stored " + cmf.getStorageDescription());
					}
				}
			}
		}
	}

	/**
	 * Holder for a Map of Spring MultipartFiles and a Map of multipart parameters.
	 */
	protected static class MultipartParsingResult {

		private final MultiValueMap<String, MultipartFile> multipartFiles;

		private final Map<String, String[]> multipartParameters;

		private final Map<String, String> multipartParameterContentTypes;

		public MultipartParsingResult(MultiValueMap<String, MultipartFile> mpFiles,
				Map<String, String[]> mpParams, Map<String, String> mpParamContentTypes) {

			this.multipartFiles = mpFiles;
			this.multipartParameters = mpParams;
			this.multipartParameterContentTypes = mpParamContentTypes;
		}

		public MultiValueMap<String, MultipartFile> getMultipartFiles() {
			return this.multipartFiles;
		}

		public Map<String, String[]> getMultipartParameters() {
			return this.multipartParameters;
		}

		public Map<String, String> getMultipartParameterContentTypes() {
			return this.multipartParameterContentTypes;
		}
	}
	
}
