package com.hypersocket.template.events;

import org.apache.commons.lang3.ArrayUtils;

import com.hypersocket.template.TemplateResource;
import com.hypersocket.session.Session;

@SuppressWarnings("serial")
public class TemplateResourceUpdatedEvent extends
		TemplateResourceEvent {

	/**
	 * TODO You typically add attributes to the base TemplateAssignableResourceEvent
	 * class so these can be reused across all resource events.
	 */
	public static final String EVENT_RESOURCE_KEY = "<resource>.updated";

	public TemplateResourceUpdatedEvent(Object source,
			Session session, TemplateResource resource) {
		super(source, EVENT_RESOURCE_KEY, session, resource);
	}

	public TemplateResourceUpdatedEvent(Object source,
			TemplateResource resource, Throwable e, Session session) {
		super(source, EVENT_RESOURCE_KEY, resource, e, session);
	}

	public String[] getResourceKeys() {
		return ArrayUtils.add(super.getResourceKeys(), EVENT_RESOURCE_KEY);
	}
}
