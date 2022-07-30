package com.hypersocket.assignable.events;

import org.apache.commons.lang3.ArrayUtils;

import com.hypersocket.assignable.TemplateAssignableResource;
import com.hypersocket.resource.AssignableResourceEvent;
import com.hypersocket.session.Session;

@SuppressWarnings("serial")
public class TemplateAssignableResourceEvent extends AssignableResourceEvent {

//	public static final String ATTR_NAME = "attr.name";
	
	public static final String EVENT_RESOURCE_KEY = "<resource>.event";
	
	public TemplateAssignableResourceEvent(Object source, String resourceKey,
			Session session, TemplateAssignableResource resource) {
		super(source, resourceKey, true, session, resource);

		/**
		 * TODO add attributes of your resource here. Make sure all attributes
		 * have a constant string definition like the commented out example above,
		 * its important for its name to start with ATTR_ as this is picked up during 
		 * the registration process
		 */
	}

	public TemplateAssignableResourceEvent(Object source, String resourceKey,
			TemplateAssignableResource resource, Throwable e, Session session) {
		super(source, resourceKey, resource, e, session);
	}

	public String[] getResourceKeys() {
		return ArrayUtils.add(super.getResourceKeys(), EVENT_RESOURCE_KEY);
	}
}
