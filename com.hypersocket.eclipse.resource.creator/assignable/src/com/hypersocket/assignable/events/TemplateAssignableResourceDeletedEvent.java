package com.hypersocket.assignable.events;

import org.apache.commons.lang3.ArrayUtils;

import com.hypersocket.assignable.TemplateAssignableResource;
import com.hypersocket.session.Session;

@SuppressWarnings("serial")
public class TemplateAssignableResourceDeletedEvent extends
		TemplateAssignableResourceEvent {

	/**
	 * TODO rename to suit your resource and replace <resource> with lower case
	 * name of your resource.
	 * 
	 * You typically add attributes to the base TemplateAssignableResourceEvent
	 * class so these can be reused across all resource events.
	 */
	public static final String EVENT_RESOURCE_KEY = "<resource>.deleted";

	public TemplateAssignableResourceDeletedEvent(Object source,
			Session session, TemplateAssignableResource resource) {
		super(source, EVENT_RESOURCE_KEY, session, resource);
	}

	public TemplateAssignableResourceDeletedEvent(Object source,
			TemplateAssignableResource resource, Throwable e, Session session) {
		super(source, EVENT_RESOURCE_KEY, resource, e, session);
	}

	public String[] getResourceKeys() {
		return ArrayUtils.add(super.getResourceKeys(), EVENT_RESOURCE_KEY);
	}
}
