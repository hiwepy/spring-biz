package org.springframework.biz.context.event.handler;

public interface EventHandler<T> {
	
	public void handle(T event);
	
}
