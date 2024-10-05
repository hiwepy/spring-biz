package org.springframework.biz.web.client;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplateHandler;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class QueryParamsUrlTemplateHandler implements UriTemplateHandler {

	private boolean strictEncoding;
	
	/**
	 * Whether to encode characters outside the unreserved set as defined in
	 * <a href="https://tools.ietf.org/html/rfc3986#section-2">RFC 3986 Section 2</a>.
	 * This ensures a URI variable value will not contain any characters with a
	 * reserved purpose.
	 * <p>By default this is set to {@code false} in which case only characters
	 * illegal for the given URI component are encoded. For example when expanding
	 * a URI variable into a path segment the "/" character is illegal and
	 * encoded. The ";" character however is legal and not encoded even though
	 * it has a reserved purpose.
	 * @param strictEncoding whether to perform strict encoding
	 */
	public void setStrictEncoding(boolean strictEncoding) {
		this.strictEncoding = strictEncoding;
	}

	/**
	 * Whether to strictly encode any character outside the unreserved set.
	 * @return Whether to strictly encode any character outside the unreserved set.
	 */
	public boolean isStrictEncoding() {
		return this.strictEncoding;
	}
	

	@Override
	public URI expand(String uriTemplate, Object... uriVariables) {
		return null;
	}
	
	@Override
    public URI expand(String uriTemplate, Map<String, ?> uriVariables) {
		
		
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uriTemplate);
        for (Map.Entry<String, ?> varEntry : uriVariables.entrySet()) {
        	builder.queryParam(varEntry.getKey(), varEntry.getValue());
        }
		
		
        return builder.build(uriVariables);
    }
	
	protected UriComponents expandAndEncode(UriComponentsBuilder builder, Map<String, ?> uriVariables) {
		if (!isStrictEncoding()) {
			return builder.buildAndExpand(uriVariables).encode();
		}
		else {
			Map<String, ?> encodedUriVars = UriUtils.encodeUriVariables(uriVariables);
			return builder.buildAndExpand(encodedUriVars);
		}
	}

	protected UriComponents expandAndEncode(UriComponentsBuilder builder, Object[] uriVariables) {
		if (!isStrictEncoding()) {
			return builder.buildAndExpand(uriVariables).encode();
		}
		else {
			Object[] encodedUriVars = UriUtils.encodeUriVariables(uriVariables);
			return builder.buildAndExpand(encodedUriVars);
		}
	}

	protected URI createUri(UriComponents uriComponents) {
		try {
			// Avoid further encoding (in the case of strictEncoding=true)
			return new URI(uriComponents.toUriString());
		}
		catch (URISyntaxException ex) {
			throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
		}
	}

	
}
