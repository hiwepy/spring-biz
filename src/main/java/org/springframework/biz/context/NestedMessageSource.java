/*
 * Copyright (c) 2018 (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.springframework.biz.context;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

public class NestedMessageSource implements MessageSource{

	protected MessageSource[] delegates;
	protected static Logger LOG = LoggerFactory.getLogger(NestedMessageSource.class);
	public NestedMessageSource() {
    }
	
	public NestedMessageSource(MessageSource ...sources){
		this.delegates = sources;
	}
	
	@Override
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		for (MessageSource source : delegates) {
			try {
				String value = source.getMessage(code, args, defaultMessage, locale);
				if(value != null){
					return value;
				}
			} catch (Exception e) {
				// ingrone e
				LOG.warn(e.getMessage());
			}
		}
		return null;
	}

	@Override
	public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
		for (MessageSource source : delegates) {
			try {
				String value = source.getMessage(code, args, locale);
				if(value != null){
					return value;
				}
			} catch (Exception e) {
				// ingrone e
				LOG.warn(e.getMessage());
			}
		}
		return null;
	}

	@Override
	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		for (MessageSource source : delegates) {
			try {
				String value = source.getMessage(resolvable, locale);
				if(value != null){
					return value;
				}
			} catch (Exception e) {
				// ingrone e
				LOG.warn(e.getMessage());
			}
		}
		return null;
	}

	public MessageSource[] getDelegates() {
		return delegates;
	}

	public void addMessageSource(MessageSource delegate) {
		this.delegates = ArrayUtils.add(delegates, delegate);
	}

}
