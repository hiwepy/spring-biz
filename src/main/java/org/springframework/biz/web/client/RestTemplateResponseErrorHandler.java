package org.springframework.biz.web.client;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ExtractingResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

public class RestTemplateResponseErrorHandler extends ExtractingResponseErrorHandler {

	@Override
	public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
		// 异常处理入口:记录url,method
		super.handleError(url, method, response);
	}
	
	@Override
	public void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {

		if (statusCode.series() == HttpStatus.Series.CLIENT_ERROR) {
			throw new HttpClientErrorException(statusCode, response.getStatusText(), response.getHeaders(),
					getResponseBody(response), getCharset(response));
		} else if (statusCode.series() == HttpStatus.Series.SERVER_ERROR) {
			throw new HttpServerErrorException(statusCode, response.getStatusText(), response.getHeaders(),
					getResponseBody(response), getCharset(response));
		} else {
			try {
				HttpStatus.Series.valueOf(statusCode);
				super.handleError(response, statusCode);
			} catch (Exception e) {
				throw new UnknownHttpStatusCodeException(statusCode.value(), response.getStatusText(),
						response.getHeaders(), getResponseBody(response), getCharset(response));
			}
		}
	}

}
