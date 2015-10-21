package com.hypersocket.resource;

import java.util.Collection;
import java.util.Map;

import com.hypersocket.permissions.AccessDeniedException;
import com.hypersocket.properties.PropertyCategory;
import com.hypersocket.realm.Realm;

public interface TemplateResourceService extends
		AbstractResourceService<TemplateResource> {

	TemplateResource updateResource(TemplateResource resourceById, String name, Map<String,String> properties)
			throws ResourceChangeException, AccessDeniedException;

	TemplateResource createResource(String name, Realm realm, Map<String,String> properties)
			throws ResourceCreationException, AccessDeniedException;

	Collection<PropertyCategory> getPropertyTemplate() throws AccessDeniedException;

	Collection<PropertyCategory> getPropertyTemplate(TemplateResource resource)
			throws AccessDeniedException;

}
