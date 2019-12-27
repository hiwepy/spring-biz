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

import org.apache.commons.lang3.StringUtils;

/**
 * 主版本号 . 子版本号 [. 修正版本号]
 * Major_Version_Number.Minor_Version_Number[.Revision_Number]
 * https://blog.csdn.net/lynnucas/article/details/50979743
 */
public class Version implements Comparable<Version> {

	public static final String DEFAULT_VERSION = "1.0.0";
	public static final String MAX_VERSION = "99.99.99";

    private final int major;
    private final int minor;
    private final int revision;
    
    public Version(String version) {
        String tokens[] = version.split("\\.");
        if (tokens.length != 3) {
            throw new IllegalArgumentException("Invalid version " + version + ". The version must have major and minor and revision number.");
        }
        major = Integer.parseInt(tokens[0]);
        minor = Integer.parseInt(tokens[1]);
        revision = Integer.parseInt(tokens[2]);
    }
    
    @Override
    public int compareTo(Version other) {
        if (this.major > other.major) {
            return 1;
        } else if (this.major < other.major) {
            return -1;
        } else if (this.minor > other.minor) {
            return 1;
        } else if (this.minor < other.minor) {
            return -1;
        }  else if (this.revision > other.revision) {
            return 1;
        } else if (this.revision < other.revision) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "v" + major + "." + minor + "." + revision;
    }
	
	public static String getNextVer(String version) {
		if(StringUtils.isEmpty(version)) {
			return DEFAULT_VERSION;
		}
		 String[] array = getVersion(version).split("\\.");
		 // 从后往前进行版本号计算
		 for(int i = (array.length -1);i > 0 ; i--) {
			 // 判断当前位是否 >= 9
			 if( i > 0 && Integer.parseInt(array[i]) >= 9) {
				 array[i] = "0";
				 array[i -1] = String.valueOf(Integer.parseInt(array[i -1]) + 1);
			 } else {
				 array[i] = String.valueOf(Integer.parseInt(array[i]) + 1);
			 }
		 }
		 return StringUtils.join(array, ".");
	}
	
    /**
     * @param version string value of value
     * @return The number of major version.
     */
    public static int getMajorVersion(String version) {
        String[] c = getVersion(version).split("\\.");
        return (c.length > 0) ? Integer.parseInt(c[0]) : 1;
    }

    /**
     * @param version string value of value
     * @return The number of minor version.
     */
    public static int getMinorVersion(String version) {
        String[] c = getVersion(version).split("\\.");
        return (c.length > 1) ? Integer.parseInt(c[1]) : 0;
    }
    
    /**
     * @param version string value of value
     * @return The number of revision version.
     */
    public static int getRevisionVersion(String version) {
        String[] c = getVersion(version).split("\\.");
        return (c.length > 2) ? Integer.parseInt(c[2]) : 0;
    }

    /**
     * @param version string value of value
     * @return The version.
     */
    private static String getVersion(String version) {
        return version.trim().replaceAll("[^0-9\\.]", "");
    }
    
}
