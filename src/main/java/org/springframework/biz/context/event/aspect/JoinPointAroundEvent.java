package org.springframework.biz.context.event.aspect;

import org.springframework.biz.context.event.EventInvocation;

@SuppressWarnings("serial")
public class JoinPointAroundEvent extends AbstractJoinPointEvent {

	public JoinPointAroundEvent(Object source, EventInvocation invocation) {
		super(source, invocation);
	}
	
}
