package org.springframework.biz.context.event.aspect;

import org.springframework.biz.context.event.EventInvocation;

@SuppressWarnings("serial")
public class JoinPointBeforeEvent extends AbstractJoinPointEvent {

	public JoinPointBeforeEvent(Object source, EventInvocation invocation) {
		super(source, invocation);
	}
	
}
