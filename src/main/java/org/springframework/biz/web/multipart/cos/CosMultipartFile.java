package org.springframework.biz.web.multipart.cos;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementation of Spring's MultipartFile interface on top of a COS
 * MultipartRequest object.
 */
@SuppressWarnings("serial")
public class CosMultipartFile implements MultipartFile, Serializable {

	protected static final Logger logger = LoggerFactory.getLogger(CosMultipartFile.class);

	private final File file;
	
	private final long size;

	private boolean preserveFilename = false;
	
	private final String fieldName;
	
	private final String filesystemName;

	private final String contentType;

	public CosMultipartFile(String fieldName, File file, String filesystemName,String contentType) {
		this.file = file;
		this.size = (this.file != null ? this.file.length() : 0);
		this.fieldName = fieldName;
		this.filesystemName = filesystemName;
		this.contentType = contentType;
	}

	/**
	 * Return the underlying {@code java.io.File} instance. There is hardly any need
	 * to access this.
	 */
	public final File getFile() {
		return this.file;
	}

	/**
	 * Set whether to preserve the filename as sent by the client, not stripping off
	 * path information in {@link CosMultipartFile#getOriginalFilename()}.
	 * <p>
	 * Default is "false", stripping off path information that may prefix the actual
	 * filename e.g. from Opera. Switch this to "true" for preserving the
	 * client-specified filename as-is, including potential path separators.
	 * 
	 * @since 4.3.5
	 * @see #getOriginalFilename()
	 * @see CosMultipartResolver#setPreserveFilename(boolean)
	 */
	public void setPreserveFilename(boolean preserveFilename) {
		this.preserveFilename = preserveFilename;
	}

	@Override
	public String getName() {
		return this.fieldName;
	}

	@Override
	public String getOriginalFilename() {

		String filename = filesystemName;
		if (filename == null) {
			// Should never happen.
			return "";
		}
		if (this.preserveFilename) {
			// Do not try to strip off a path...
			return filename;
		}

		// Check for Unix-style path
		int unixSep = filename.lastIndexOf("/");
		// Check for Windows-style path
		int winSep = filename.lastIndexOf("\\");
		// Cut off at latest possible point
		int pos = (winSep > unixSep ? winSep : unixSep);
		if (pos != -1) {
			// Any sort of path separator found...
			return filename.substring(pos + 1);
		} else {
			// A plain name
			return filename;
		}
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public boolean isEmpty() {
		return (this.size == 0);
	}

	@Override
	public long getSize() {
		return this.size;
	}

	@Override
	public byte[] getBytes() throws IOException {
		if (this.file != null && !this.file.exists()) {
			throw new IllegalStateException("File has been moved - cannot be read again");
		}
		return (this.size > 0 ? FileCopyUtils.copyToByteArray(this.file) : new byte[0]);
	}

	public InputStream getInputStream() throws IOException {
		if (this.file != null && !this.file.exists()) {
			throw new IllegalStateException("File has been moved - cannot be read again");
		}
		if (this.size != 0) {
			return new FileInputStream(this.file);
		} else {
			return new ByteArrayInputStream(new byte[0]);
		}
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {

		if (this.file != null && !this.file.exists()) {
			throw new IllegalStateException("File has already been moved - cannot be transferred again");
		}

		if (dest.exists() && !dest.delete()) {
			throw new IOException(
					"Destination file [" + dest.getAbsolutePath() + "] already exists and could not be deleted");
		}

		try {
			boolean moved = false;
			if (this.file != null) {
				moved = this.file.renameTo(dest);
				if (!moved) {
					FileCopyUtils.copy(this.file, dest);
				}
			} else {
				dest.createNewFile();
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Multipart file '" + getName() + "' with original filename [" + getOriginalFilename()
						+ "], stored " + (this.file != null ? "at [" + this.file.getAbsolutePath() + "]" : "in memory")
						+ ": " + (moved ? "moved" : "copied") + " to [" + dest.getAbsolutePath() + "]");
			}

		} catch (IllegalStateException ex) {
			// Pass through when coming from FileItem directly
			throw ex;
		} catch (IOException ex) {
			// From I/O operations within FileItem.write
			throw ex;
		} catch (Exception ex) {
			throw new IOException("File transfer failed", ex);
		}
	}

	/**
	 * Return a description for the storage location of the multipart content.
	 * Tries to be as specific as possible: mentions the file location in case
	 * of a temporary file.
	 */
	public String getStorageDescription() {
		if (this.file.exists()) {
			return "at [" + file.getAbsolutePath() + "]";
		}
		else {
			return "on disk";
		}
	}

}
