package com.hypersocket.template.events;

import org.apache.commons.lang3.ArrayUtils;

import com.hypersocket.template.TemplateResource;
import com.hypersocket.session.Session;

public class TemplateResourceCreatedEvent extends
		TemplateResourceEvent {

	/**
	 * TODO rename to suit your resource and replace <resource> with lower case
	 * name of your resource.
	 * 
	 * You typically add attributes to the base TemplateAssignableResourceEvent class
	 * so these can be reused across all resource events.
	 */
	public static final String EVENT_RESOURCE_KEY = "<resource>.created";
	
	public TemplateResourceCreatedEvent(Object source,
			Session session,
			TemplateResource resource) {
		super(source, EVENT_RESOURCE_KEY, session, resource);
	}

	public TemplateResourceCreatedEvent(Object source,
			TemplateResource resource, Throwable e,
			Session session) {
		super(source, EVENT_RESOURCE_KEY, resource, e, session);
	}

	public String[] getResourceKeys() {
		return ArrayUtils.add(super.getResourceKeys(), EVENT_RESOURCE_KEY);
	}
}
