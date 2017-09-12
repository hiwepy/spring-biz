package org.springframework.biz.web.multipart.cos;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.oreilly.servlet.multipart.FileRenamePolicy;
import com.oreilly.servlet.multipart.MultipartParser;

/**
 * A utility class to handle <code>multipart/form-data</code> requests, the kind
 * of requests that support file uploads. This class emulates the interface of
 * <code>HttpServletRequest</code>, making it familiar to use. It uses a "push"
 * model where any incoming files are read and saved directly to disk in the
 * constructor. If you wish to have more flexibility, e.g. write the files to a
 * database, use the "pull" model <code>MultipartParser</code> instead.
 * <p>
 * This class can receive arbitrarily large files (up to an artificial limit you
 * can set), and fairly efficiently too. It cannot handle nested data (multipart
 * content within multipart content). It <b>can</b> now with the latest release
 * handle internationalized content (such as non Latin-1 filenames).
 * <p>
 * To avoid collisions and have fine control over file placement, there's a
 * constructor variety that takes a pluggable FileRenamePolicy implementation. A
 * particular policy can choose to rename or change the location of the file
 * before it's written.
 * <p>
 * See the included upload.war for an example of how to use this class.
 * <p>
 * The full file upload specification is contained in experimental RFC 1867,
 * available at
 * <a href="http://www.ietf.org/rfc/rfc1867.txt"> http://www.ietf.org/rfc/
 * rfc1867.txt</a>.
 *
 * @see MultipartParser
 * 
 * @author Jason Hunter
 * @author Geoff Soutter
 * @version 1.12, 2004/04/11, added null check for Opera malformed bug<br>
 * @version 1.11, 2002/11/01, combine query string params in param list<br>
 * @version 1.10, 2002/05/27, added access to the original file names<br>
 * @version 1.9, 2002/04/30, added support for file renaming, thanks to
 *          Changshin Lee<br>
 * @version 1.8, 2002/04/30, added support for internationalization, thanks to
 *          Changshin Lee<br>
 * @version 1.7, 2001/02/07, made fields protected to increase user flexibility
 *          <br>
 * @version 1.6, 2000/07/21, redid internals to use MultipartParser, thanks to
 *          Geoff Soutter<br>
 * @version 1.5, 2000/02/04, added auto MacBinary decoding for IE on Mac<br>
 * @version 1.4, 2000/01/05, added getParameterValues(), WebSphere 2.x
 *          getContentType() workaround, stopped writing empty "unknown" file
 *          <br>
 * @version 1.3, 1999/12/28, IE4 on Win98 lastIndexOf("boundary=") workaround
 *          <br>
 * @version 1.2, 1999/12/20, IE4 on Mac readNextPart() workaround<br>
 * @version 1.1, 1999/01/15, JSDK readLine() bug workaround<br>
 * @version 1.0, 1998/09/18<br>
 */
public class CosMultipartRequest implements MultipartRequest {

	private static final int DEFAULT_MAX_POST_SIZE = 1024 * 1024; // 1 Meg
	
	/**
	 * Constructs a new MultipartRequest to handle the specified request, saving
	 * any uploaded files to the given directory, and limiting the upload size
	 * to 1 Megabyte. If the content is too large, an IOException is thrown.
	 * This constructor actually parses the <tt>multipart/form-data</tt> and
	 * throws an IOException if there's any problem reading or parsing the
	 * request.
	 *
	 * @param request
	 *            the servlet request.
	 * @param saveDirectory
	 *            the directory in which to save any uploaded files.
	 * @exception IOException
	 *                if the uploaded content is larger than 1 Megabyte or
	 *                there's a problem reading or parsing the request.
	 */
	public CosMultipartRequest(HttpServletRequest request, String saveDirectory) throws IOException {

		this(request, saveDirectory, DEFAULT_MAX_POST_SIZE);

	}

	/**
	 * Constructs a new MultipartRequest to handle the specified request, saving
	 * any uploaded files to the given directory, and limiting the upload size
	 * to the specified length. If the content is too large, an IOException is
	 * thrown. This constructor actually parses the <tt>multipart/form-data</tt>
	 * and throws an IOException if there's any problem reading or parsing the
	 * request.
	 *
	 * @param request
	 *            the servlet request.
	 * @param saveDirectory
	 *            the directory in which to save any uploaded files.
	 * @param maxPostSize
	 *            the maximum size of the POST content.
	 * @exception IOException
	 *                if the uploaded content is larger than
	 *                <tt>maxPostSize</tt> or there's a problem reading or
	 *                parsing the request.
	 */
	public CosMultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize) throws IOException {

		this(request, saveDirectory, maxPostSize, null, null);

	}

	/**
	 * Constructs a new MultipartRequest to handle the specified request, saving
	 * any uploaded files to the given directory, and limiting the upload size
	 * to the specified length. If the content is too large, an IOException is
	 * thrown. This constructor actually parses the <tt>multipart/form-data</tt>
	 * and throws an IOException if there's any problem reading or parsing the
	 * request.
	 *
	 * @param request
	 *            the servlet request.
	 * @param saveDirectory
	 *            the directory in which to save any uploaded files.
	 * @param encoding
	 *            the encoding of the response, such as ISO-8859-1
	 * @exception IOException
	 *                if the uploaded content is larger than 1 Megabyte or
	 *                there's a problem reading or parsing the request.
	 */
	public CosMultipartRequest(HttpServletRequest request, String saveDirectory, String encoding) throws IOException {

		this(request, saveDirectory, DEFAULT_MAX_POST_SIZE, encoding, null);

	}

	/**
	 * Constructs a new MultipartRequest to handle the specified request, saving
	 * any uploaded files to the given directory, and limiting the upload size
	 * to the specified length. If the content is too large, an IOException is
	 * thrown. This constructor actually parses the <tt>multipart/form-data</tt>
	 * and throws an IOException if there's any problem reading or parsing the
	 * request.
	 *
	 * @param request
	 *            the servlet request.
	 * @param saveDirectory
	 *            the directory in which to save any uploaded files.
	 * @param maxPostSize
	 *            the maximum size of the POST content.
	 * @param policy
	 *            the rules for renaming in case of file name collisions
	 * @exception IOException
	 *                if the uploaded content is larger than
	 *                <tt>maxPostSize</tt> or there's a problem reading or
	 *                parsing the request.
	 */
	public CosMultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize,
			FileRenamePolicy policy) throws IOException {

		this(request, saveDirectory, maxPostSize, null, policy);

	}

	/**
	 * Constructs a new MultipartRequest to handle the specified request, saving
	 * any uploaded files to the given directory, and limiting the upload size
	 * to the specified length. If the content is too large, an IOException is
	 * thrown. This constructor actually parses the <tt>multipart/form-data</tt>
	 * and throws an IOException if there's any problem reading or parsing the
	 * request.
	 *
	 * @param request
	 *            the servlet request.
	 * @param saveDirectory
	 *            the directory in which to save any uploaded files.
	 * @param maxPostSize
	 *            the maximum size of the POST content.
	 * @param encoding
	 *            the encoding of the response, such as ISO-8859-1
	 * @exception IOException
	 *                if the uploaded content is larger than
	 *                <tt>maxPostSize</tt> or there's a problem reading or
	 *                parsing the request.
	 */
	public CosMultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize, String encoding)
			throws IOException {

		this(request, saveDirectory, maxPostSize, encoding, null);

	}

	 /**
	   * Constructs a new MultipartRequest to handle the specified request, 
	   * saving any uploaded files to the given directory, and limiting the 
	   * upload size to the specified length.  If the content is too large, an 
	   * IOException is thrown.  This constructor actually parses the 
	   * <tt>multipart/form-data</tt> and throws an IOException if there's any 
	   * problem reading or parsing the request.
	   *
	   * To avoid file collisions, this constructor takes an implementation of the
	   * FileRenamePolicy interface to allow a pluggable rename policy.
	   *
	   * @param request the servlet request.
	   * @param saveDirectory the directory in which to save any uploaded files.
	   * @param maxPostSize the maximum size of the POST content.
	   * @param encoding the encoding of the response, such as ISO-8859-1
	   * @param policy a pluggable file rename policy
	   * @exception IOException if the uploaded content is larger than 
	   * <tt>maxPostSize</tt> or there's a problem reading or parsing the request.
	   */
	  public CosMultipartRequest(HttpServletRequest request,
	                          String saveDirectory,
	                          int maxPostSize,
	                          String encoding,
	                          FileRenamePolicy policy) throws IOException {
	 
	    // Sanity check values
	    if (request == null)
	      throw new IllegalArgumentException("request cannot be null");
	    if (saveDirectory == null)
	      throw new IllegalArgumentException("saveDirectory cannot be null");
	    if (maxPostSize <= 0) {
	 
	      throw new IllegalArgumentException("maxPostSize must be positive");
	    }
	    
	}

	@Override
	public Iterator<String> getFileNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MultipartFile getFile(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MultipartFile> getFiles(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, MultipartFile> getFileMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MultiValueMap<String, MultipartFile> getMultiFileMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMultipartContentType(String paramOrFileName) {
		// TODO Auto-generated method stub
		return null;
	}
 

}
