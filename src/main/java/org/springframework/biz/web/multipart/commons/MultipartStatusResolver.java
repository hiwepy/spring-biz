package org.springframework.biz.web.multipart.commons;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

/**
 * 
 * <b> Spring中带进度条的文件上传</b>
 * <p> 1、Spring的DispatcherServlet在初始化的时候会去容器中查找是否有可用的MultipartResolver，如果有的话就会使用此resolver将request转换为MultipartHttpServletRequest。</p> 
 * <p> 2、Spring提供了两个resolver，CommonsMultipartResolver，StandardServletMultipartResolver。我们可以任选其一。
 *	CommonsMultipartResolver的parseRequest方法调用commons-fileupload的ServletFileupload完成了对request的解析工作。</p>
 * <p> 3、最后在controller的配置文件中指定resolver： &lt;bean id="multipartResolver" class="org.springframework.biz.web.multipart.commons.MultipartStatusResolver"&gt;&lt;/bean&gt;</p>
 */
public class MultipartStatusResolver extends CommonsMultipartResolver {
	
	@Override
    public MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        String encoding = "utf-8";
        FileUpload fileUpload = super.prepareFileUpload(encoding);
        final HttpSession session = request.getSession();
        fileUpload.setProgressListener(new ProgressListener() {
            public void update(long pBytesRead, long pContentLength, int pItems) {
                int percent = (int) (((float)pBytesRead / (float)pContentLength) * 100);
                session.setAttribute("percent", percent + "%");
            }
        });
        
        try {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            return super.parseFileItems(fileItems, encoding);
        }
        catch (FileUploadBase.SizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        }
        catch (FileUploadException ex) {
            throw new MultipartException("Could not parse multipart servlet request", ex);
        }
    }
	
}
