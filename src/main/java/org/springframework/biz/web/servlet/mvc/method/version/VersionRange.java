/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
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

public class VersionRange {

    private Version from;
    private Version to;

    public VersionRange(String from, String to) {
        this.from = new Version(from);
        this.to = new Version(to);
    }

    public boolean includes(String other) {
        Version otherVersion = new Version(other);

        if (from.compareTo(otherVersion) <= 0 && to.compareTo(otherVersion) >= 0) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "range[" + from + "-" + to + "]";
    }
}