package com.hypersocket.template;

import org.springframework.stereotype.Repository;

import com.hypersocket.resource.AbstractResourceRepositoryImpl;

@Repository
public class TemplateResourceRepositoryImpl extends
		AbstractResourceRepositoryImpl<TemplateResource> implements
		TemplateResourceRepository {

	@Override
	protected Class<TemplateResource> getResourceClass() {
		return TemplateResource.class;
	}

}
