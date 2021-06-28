package org.springframework.biz.web.multipart.cos;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.oreilly.servlet.multipart.FileRenamePolicy;

import hitool.core.lang3.iterator.EnumerationIterator;

public class CosMultipartRequest implements MultipartRequest {

	protected static final int DEFAULT_MAX_POST_SIZE = 1024 * 1024; // 1 Meg
	protected static final Logger LOG = LoggerFactory.getLogger(CosMultipartRequest.class);
	private com.oreilly.servlet.MultipartRequest multi;
	private String defaultEncoding;
	private boolean maxSizeProvided;
	private int maxSize;
	private boolean preserveFilename = false;
	
	public CosMultipartRequest(HttpServletRequest request, String saveDirectory) throws IOException {
		this(request, saveDirectory, DEFAULT_MAX_POST_SIZE);
	}

	public CosMultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize) throws IOException {
		this(request, saveDirectory, maxPostSize, null, null);
	}

	public CosMultipartRequest(HttpServletRequest request, String saveDirectory, String encoding) throws IOException {
		this(request, saveDirectory, DEFAULT_MAX_POST_SIZE, encoding, null);
	}

	public CosMultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize,
			FileRenamePolicy policy) throws IOException {
		this(request, saveDirectory, maxPostSize, null, policy);
	}

	public CosMultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize, String encoding)
			throws IOException {
		this(request, saveDirectory, maxPostSize, encoding, null);
	}

	public CosMultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize, String encoding,
			FileRenamePolicy policy) throws IOException {

		// Sanity check values
		if (request == null)
			throw new IllegalArgumentException("request cannot be null");
		if (saveDirectory == null)
			throw new IllegalArgumentException("saveDirectory cannot be null");
		if (maxPostSize <= 0) {
			throw new IllegalArgumentException("maxPostSize must be positive");
		}
		
		this.maxSize = maxPostSize;
		this.defaultEncoding = encoding;
		
		if (maxSizeProvided) {
			multi = new com.oreilly.servlet.MultipartRequest(request, saveDirectory, maxSize, defaultEncoding);
		} else {
			multi = new com.oreilly.servlet.MultipartRequest(request, saveDirectory, defaultEncoding);
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	public Iterator<String> getFileNames() {
		return CollectionUtils.toIterator(multi.getFileNames());
	}

	@Override
	public MultipartFile getFile(String fieldName) {
		CosMultipartFile multipartFile = new CosMultipartFile(fieldName , getOriginalFile(fieldName), getFilesystemName(fieldName), getMultipartContentType(fieldName));
		multipartFile.setPreserveFilename(this.preserveFilename);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found multipart file [" + multipartFile.getName() + "] of size " + multipartFile.getSize() +
					" bytes with original filename [" + multipartFile.getOriginalFilename() + "], stored " +
					multipartFile.getStorageDescription());
		}
		return multipartFile;
	}
	
	public File getOriginalFile(String fieldName) {
		return multi.getFile(fieldName);
	}
	
	public String getFilesystemName(String fieldName) {
		return multi.getFilesystemName(fieldName);
	}

	public String getOriginalFileName(String fieldName) {
		return multi.getOriginalFileName(fieldName);
	}

	@Override
	public List<MultipartFile> getFiles(String fieldName) {
		List<MultipartFile> files = new LinkedList<MultipartFile>();
		files.add(getFile(fieldName));
		return files;
	}
	
	@Override
	public Map<String, MultipartFile> getFileMap() {
		Map<String, MultipartFile> multipartFiles = new LinkedHashMap<String, MultipartFile>();
		Iterator<String> fileNames = this.getFileNames();
		while(fileNames !=null && fileNames.hasNext()) {
			String fieldName = fileNames.next();
			multipartFiles.put(fieldName, this.getFile(fieldName));
		}
		return multipartFiles;
	}
	
	@Override
	public MultiValueMap<String, MultipartFile> getMultiFileMap() {
		MultiValueMap<String, MultipartFile> multipartFiles = new LinkedMultiValueMap<String, MultipartFile>();
		Iterator<String> fileNames = this.getFileNames();
		while(fileNames !=null && fileNames.hasNext()) {
			String fieldName = fileNames.next();
			multipartFiles.add(fieldName, this.getFile(fieldName));
		}
		return multipartFiles;
	}

	@Override
	public String getMultipartContentType(String paramOrFileName) {
		return multi.getContentType(paramOrFileName);
	}

	@SuppressWarnings("rawtypes")
	public Map<String, String[]> getMultipartParameters() {
		Map<String, String[]> multipartParameters = new HashMap<String, String[]>();
		Enumeration enumeration = multi.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String fieldName = (String) enumeration.nextElement();
			multipartParameters.put(fieldName, multi.getParameterValues(fieldName));
		}
		return multipartParameters;
	}
	
	@SuppressWarnings("rawtypes")
	public Map<String, String> getMultipartParameterContentTypes() {
		Map<String, String> multipartParameterContentTypes = new HashMap<String, String>();
		Enumeration enumeration = multi.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String fieldName = (String) enumeration.nextElement();
			multipartParameterContentTypes.put(fieldName, multi.getContentType(fieldName));
		}
		return multipartParameterContentTypes;
	}
	
	public String MultipartParameter(String fieldName) {
		String value;
		String partEncoding = determineEncoding(multi.getContentType(fieldName), defaultEncoding);
		if (partEncoding != null) {
			value = multi.getParameter(partEncoding);
			if(!StringUtils.hasText(value)) {
				if (LOG.isWarnEnabled()) {
					LOG.warn("Could not decode multipart item '" + fieldName +
							"' with encoding '" + partEncoding + "': using platform default");
				}
				value = multi.getParameter(fieldName);
			}
		}
		else {
			value = multi.getParameter(fieldName);
		}
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<String> getParameterNames() {
		return CollectionUtils.toIterator(multi.getParameterNames());
	}
	
	public String getParameter(String fieldName) {
		return multi.getParameter(fieldName);
	}

	public String[] getParameterValues(String fieldName) {
		return multi.getParameterValues(fieldName);
	}

	public void setPreserveFilename(boolean preserveFilename) {
		this.preserveFilename = preserveFilename;
	}
	
	private String determineEncoding(String contentTypeHeader, String defaultEncoding) {
		if (!StringUtils.hasText(contentTypeHeader)) {
			return defaultEncoding;
		}
		MediaType contentType = MediaType.parseMediaType(contentTypeHeader);
		Charset charset = contentType.getCharset();
		return (charset != null ? charset.name() : defaultEncoding);
	}
	
}
