package com.hypersocket.resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TemplateResourceRepositoryImpl extends
		AbstractResourceRepositoryImpl<TemplateResource> implements
		TemplateResourceRepository {

	@Override
	protected Class<TemplateResource> getResourceClass() {
		return TemplateResource.class;
	}

}
