package org.springframework.biz.web.servlet.view.freemarker.configurer;

import freemarker.cache.TemplateLoader;
import org.springframework.biz.web.servlet.view.freemarker.cache.HtmlTemplateLoader;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.List;

public class EscapeFreeMarkerConfigurer extends FreeMarkerConfigurer{  
    
  @Override  
  protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders) {  
      return new HtmlTemplateLoader(super.getAggregateTemplateLoader(templateLoaders));  
  }  
  
}
