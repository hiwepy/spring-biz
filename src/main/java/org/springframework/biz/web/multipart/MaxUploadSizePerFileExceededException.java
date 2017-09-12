package org.springframework.biz.web.multipart;

import org.springframework.web.multipart.MultipartException;

@SuppressWarnings("serial")
public class MaxUploadSizePerFileExceededException extends MultipartException {

	private final long maxUploadSizePerFile;
	
	public MaxUploadSizePerFileExceededException(long maxUploadSizePerFile) {
		this(maxUploadSizePerFile, null);
	}

	public MaxUploadSizePerFileExceededException(long maxUploadSizePerFile, Throwable ex) {
		super("Maximum upload size (in bytes) of " + maxUploadSizePerFile + " bytes exceeded", ex);
		this.maxUploadSizePerFile = maxUploadSizePerFile;
	}

	/**
	 * Return the maximum upload size (in bytes) for each individual file
	 */
	public long getMaxUploadSizePerFile() {
		return this.maxUploadSizePerFile;
	}

}
