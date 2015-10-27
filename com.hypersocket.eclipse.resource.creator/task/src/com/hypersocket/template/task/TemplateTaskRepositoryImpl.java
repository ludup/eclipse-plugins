package com.hypersocket.template.task;

import javax.annotation.PostConstruct;

import com.hypersocket.properties.ResourceTemplateRepositoryImpl;

public class TemplateTaskRepositoryImpl extends
		ResourceTemplateRepositoryImpl implements TemplateTaskRepository {

	@PostConstruct
	private void postConstruct() {
		loadPropertyTemplates("tasks/<resource>Task.xml");
	}

}
