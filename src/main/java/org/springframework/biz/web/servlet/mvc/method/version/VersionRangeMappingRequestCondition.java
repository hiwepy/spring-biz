/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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
package org.springframework.biz.web.servlet.mvc.method.version;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;

public class VersionRangeMappingRequestCondition extends AbstractRequestCondition<VersionRangeMappingRequestCondition> {
	
	private Logger logger = LoggerFactory.getLogger(VersionRangeMappingRequestCondition.class);
	private final Set<VersionRange> versions;
	private final String acceptedMediaType;

	public VersionRangeMappingRequestCondition(String acceptedMediaType, String from, String to) {
		this(acceptedMediaType, versionRange(from, to));
	}

	public VersionRangeMappingRequestCondition(String acceptedMediaType, Collection<VersionRange> versions) {
		this.acceptedMediaType = acceptedMediaType;
		this.versions = Collections.unmodifiableSet(new HashSet<VersionRange>(versions));
	}

	private static Set<VersionRange> versionRange(String from, String to) {
		HashSet<VersionRange> versionRanges = new HashSet<>();

		if (StringUtils.hasText(from)) {
			String toVersion = (StringUtils.hasText(to) ? to : Version.MAX_VERSION);
			VersionRange versionRange = new VersionRange(from, toVersion);
			versionRanges.add(versionRange);
		}

		return versionRanges;
	}

	@Override
	public VersionRangeMappingRequestCondition combine(VersionRangeMappingRequestCondition other) {
		logger.debug("Combining:\n{}\n{}", this, other);
		Set<VersionRange> newVersions = new LinkedHashSet<VersionRange>(this.versions);
		newVersions.addAll(other.versions);
		String newMediaType;
		if (StringUtils.hasText(this.acceptedMediaType) && StringUtils.hasText(other.acceptedMediaType)
				&& !this.acceptedMediaType.equals(other.acceptedMediaType)) {
			throw new IllegalArgumentException("Both conditions should have the same media type. "
					+ this.acceptedMediaType + " =!= " + other.acceptedMediaType);
		} else if (StringUtils.hasText(this.acceptedMediaType)) {
			newMediaType = this.acceptedMediaType;
		} else {
			newMediaType = other.acceptedMediaType;
		}
		return new VersionRangeMappingRequestCondition(newMediaType, newVersions);
	}

	@Override
	public VersionRangeMappingRequestCondition getMatchingCondition(HttpServletRequest request) {
		
		String accept = request.getHeader("Accept");
		Pattern regexPattern = Pattern.compile("(.*)-(\\d+\\.\\d+\\.\\d+).*");
		Matcher matcher = regexPattern.matcher(accept);
		if (matcher.matches()) {
			String actualMediaType = matcher.group(1);
			String version = matcher.group(2);
			logger.debug("Version={}", version);

			if (acceptedMediaType.startsWith(actualMediaType)) {

				for (VersionRange versionRange : versions) {
					if (versionRange.includes(version)) {
						return this;
					}
				}
			}
		}

		logger.debug("Didn't find matching version");
		return null;
	}

	@Override
	public int compareTo(VersionRangeMappingRequestCondition other, HttpServletRequest request) {
		return 0;
	}

	@Override
	protected Collection<?> getContent() {
		return versions;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("version={");
		sb.append("media=").append(acceptedMediaType).append(",");
		for (VersionRange range : versions) {
			sb.append(range).append(",");
		}
		sb.append("}");

		return sb.toString();
	}

	@Override
	protected String getToStringInfix() {
		return " && ";
	}
}