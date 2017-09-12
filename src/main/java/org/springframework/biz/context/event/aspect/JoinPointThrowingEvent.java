package org.springframework.biz.context.event.aspect;

import org.springframework.biz.context.event.EventInvocation;

@SuppressWarnings("serial")
public class JoinPointThrowingEvent extends AbstractJoinPointEvent {

	public JoinPointThrowingEvent(Object source, EventInvocation invocation) {
		super(source, invocation);
	}
	
}
