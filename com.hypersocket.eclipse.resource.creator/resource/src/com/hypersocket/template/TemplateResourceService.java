/*******************************************************************************
 * Copyright (c) 2019 LogonBox Limited.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package com.hypersocket.template;

import java.util.Collection;
import java.util.Map;

import com.hypersocket.permissions.AccessDeniedException;
import com.hypersocket.properties.PropertyCategory;
import com.hypersocket.realm.Realm;
import com.hypersocket.resource.AbstractResourceService;
import com.hypersocket.resource.ResourceException;

public interface TemplateResourceService extends
		AbstractResourceService<TemplateResource> {

	TemplateResource updateResource(TemplateResource resourceById, String name, Map<String,String> properties)
			throws ResourceException, AccessDeniedException;

	TemplateResource createResource(String name, Realm realm, Map<String,String> properties)
			throws ResourceException, AccessDeniedException;

	Collection<PropertyCategory> getPropertyTemplate() throws AccessDeniedException;

	Collection<PropertyCategory> getPropertyTemplate(TemplateResource resource)
			throws AccessDeniedException;

}
