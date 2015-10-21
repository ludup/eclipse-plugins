package com.hypersocket.resource.events;

import org.apache.commons.lang3.ArrayUtils;

import com.hypersocket.realm.events.ResourceEvent;
import com.hypersocket.resource.TemplateResource;
import com.hypersocket.session.Session;

public class TemplateResourceEvent extends ResourceEvent {

	public static final String EVENT_RESOURCE_KEY = "<resource>.event";
	
	public TemplateResourceEvent(Object source, String resourceKey,
			Session session, TemplateResource resource) {
		super(source, resourceKey, true, session, resource);

		/**
		 * TODO add attributes of your resource here. Make sure all attributes
		 * have a constant string definition like the commented out example above,
		 * its important for its name to start with ATTR_ as this is picked up during 
		 * the registration process
		 */
	}

	public TemplateResourceEvent(Object source, String resourceKey,
			TemplateResource resource, Throwable e, Session session) {
		super(source, resourceKey, e, session, resource);
	}
	
	public String[] getResourceKeys() {
		return ArrayUtils.add(super.getResourceKeys(), EVENT_RESOURCE_KEY);
	}
}
