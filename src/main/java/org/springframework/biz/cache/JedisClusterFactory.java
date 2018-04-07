/*
 * Copyright (c) 2018 (https://github.com/vindell).
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
package org.springframework.biz.cache;

import java.util.HashSet;  
import java.util.Properties;  
import java.util.Set;  
import java.util.regex.Pattern;  
 
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;  
import org.springframework.beans.factory.FactoryBean;  
import org.springframework.beans.factory.InitializingBean;  
import org.springframework.core.io.Resource;  
 
import redis.clients.jedis.HostAndPort;  
import redis.clients.jedis.JedisCluster;  
 
public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {  
 
   private Resource addressConfig;  
   private String addressKeyPrefix ;  
 
   private JedisCluster jedisCluster;  
   private Integer timeout;  
   private Integer maxRedirections;  
   private GenericObjectPoolConfig genericObjectPoolConfig;  
     
   private Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");  
 
   @Override  
   public JedisCluster getObject() throws Exception {  
       return jedisCluster;  
   }  
 
   @Override  
   public Class<? extends JedisCluster> getObjectType() {  
       return (this.jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class);  
   }  
 
   @Override  
   public boolean isSingleton() {  
       return true;  
   }  
 
 
 
   private Set<HostAndPort> parseHostAndPort() throws Exception {  
       try {  
           Properties prop = new Properties();  
           prop.load(this.addressConfig.getInputStream());  
 
           Set<HostAndPort> haps = new HashSet<HostAndPort>();  
           for (Object key : prop.keySet()) {  
 
               if (!((String) key).startsWith(addressKeyPrefix)) {  
                   continue;  
               }  
 
               String val = (String) prop.get(key);  
 
               boolean isIpPort = p.matcher(val).matches();  
 
               if (!isIpPort) {  
                   throw new IllegalArgumentException("ip 或 port 不合法");  
               }  
               String[] ipAndPort = val.split(":");  
 
               HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));  
               haps.add(hap);  
           }  
 
           return haps;  
       } catch (IllegalArgumentException ex) {  
           throw ex;  
       } catch (Exception ex) {  
           throw new Exception("解析 jedis 配置文件失败", ex);  
       }  
   }  
     
   @Override  
   public void afterPropertiesSet() throws Exception {  
       Set<HostAndPort> haps = this.parseHostAndPort();  
         
       jedisCluster = new JedisCluster(haps, timeout, maxRedirections, genericObjectPoolConfig);  
         
   }  
   public void setAddressConfig(Resource addressConfig) {  
       this.addressConfig = addressConfig;  
   }  
 
   public void setTimeout(int timeout) {  
       this.timeout = timeout;  
   }  
 
   public void setMaxRedirections(int maxRedirections) {  
       this.maxRedirections = maxRedirections;  
   }  
 
   public void setAddressKeyPrefix(String addressKeyPrefix) {  
       this.addressKeyPrefix = addressKeyPrefix;  
   }  
 
   public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {  
       this.genericObjectPoolConfig = genericObjectPoolConfig;  
   }  
 
} 
