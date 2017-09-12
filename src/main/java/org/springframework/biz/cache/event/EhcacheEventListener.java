package org.springframework.biz.cache.event;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListenerAdapter;

public class EhcacheEventListener extends CacheEventListenerAdapter {

	public void notifyElementRemoved(Ehcache cache, Element element) throws CacheException {
	}

	public void notifyElementPut(Ehcache cache, Element element) throws CacheException {
	}

	public void notifyElementUpdated(Ehcache cache, Element element) throws CacheException {
	}

	public void notifyElementExpired(Ehcache cache, Element element) {
		
		
	}

	public void notifyElementEvicted(Ehcache cache, Element element) {
	}

	public void notifyRemoveAll(Ehcache cache) {
	}

	public void dispose() {
	}
}
