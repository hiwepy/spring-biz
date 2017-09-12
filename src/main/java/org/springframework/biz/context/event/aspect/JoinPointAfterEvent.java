package org.springframework.biz.context.event.aspect;

import org.springframework.biz.context.event.EventInvocation;

@SuppressWarnings("serial")
public class JoinPointAfterEvent extends AbstractJoinPointEvent {

	public JoinPointAfterEvent(Object source, EventInvocation invocation) {
		super(source, invocation);
	}
	
}
