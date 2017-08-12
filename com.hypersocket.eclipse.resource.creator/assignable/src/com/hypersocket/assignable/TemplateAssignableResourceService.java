package com.hypersocket.assignable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.hypersocket.permissions.AccessDeniedException;
import com.hypersocket.permissions.Role;
import com.hypersocket.properties.PropertyCategory;
import com.hypersocket.realm.Realm;
import com.hypersocket.resource.AbstractAssignableResourceService;
import com.hypersocket.resource.ResourceChangeException;
import com.hypersocket.resource.ResourceException;


public interface TemplateAssignableResourceService extends
		AbstractAssignableResourceService<TemplateAssignableResource> {

	/**
	 * TODO rename this class to match your entity. Modify updateResource, createResource methods
	 * to take parameters for each additional field you have defined in your entity. 
	 */
	
	TemplateAssignableResource updateResource(TemplateAssignableResource resourceById, String name,
			Set<Role> roles, Map<String,String> properties) throws ResourceException, AccessDeniedException;

	TemplateAssignableResource createResource(String name, Set<Role> roles, Realm realm, Map<String,String> properties)
			throws ResourceException, AccessDeniedException;

	Collection<PropertyCategory> getPropertyTemplate(
			TemplateAssignableResource resource) throws AccessDeniedException;

	Collection<PropertyCategory> getPropertyTemplate()
			throws AccessDeniedException;


}
