package com.hypersocket.assignable;

import org.springframework.stereotype.Repository;

import com.hypersocket.resource.AbstractAssignableResourceRepositoryImpl;

@Repository
public class TemplateAssignableResourceRepositoryImpl extends
		AbstractAssignableResourceRepositoryImpl<TemplateAssignableResource> implements
		TemplateAssignableResourceRepository {

	/**
	 * TODO rename this class to match your entity / interface
	 */
	@Override
	protected Class<TemplateAssignableResource> getResourceClass() {
		return TemplateAssignableResource.class;
	}

}
