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
package org.springframework.biz.web.servlet.view.xml;

import org.springframework.oxm.Marshaller;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.AbstractJackson2View;
import org.springframework.web.servlet.view.xml.MappingJackson2XmlView;

import java.util.Locale;

public class Jackson2XmlViewResolver implements ViewResolver {

	private Marshaller marshaller;
    
    public Jackson2XmlViewResolver(Marshaller marshaller) {
        this.marshaller = marshaller;
    }
    
    
    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
    	AbstractJackson2View view = new MappingJackson2XmlView();
        return view;
    }
    
}