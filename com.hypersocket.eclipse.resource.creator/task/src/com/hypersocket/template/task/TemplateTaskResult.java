package com.hypersocket.template.task;

import org.apache.commons.lang3.ArrayUtils;

import com.hypersocket.realm.Realm;
import com.hypersocket.tasks.Task;
import com.hypersocket.triggers.AbstractTaskResult;

public class TemplateTaskResult extends AbstractTaskResult {

	public static final String EVENT_RESOURCE_KEY = "<resource>.result";
	
	public TemplateTaskResult(Object source, 
			boolean success, Realm currentRealm, Task task) {
		super(source, EVENT_RESOURCE_KEY, success, currentRealm, task);
	}

	public TemplateTaskResult(Object source, Throwable e,
			Realm currentRealm, Task task) {
		super(source, EVENT_RESOURCE_KEY, e, currentRealm, task);
	}

	@Override
	public boolean isPublishable() {
		return true;
	}

	@Override
	public String getResourceBundle() {
		return TemplateTask.RESOURCE_BUNDLE;
	}
	
	public String[] getResourceKeys() {
		return ArrayUtils.add(super.getResourceKeys(), EVENT_RESOURCE_KEY);
	}

}
