package com.hypersocket.template.task;

import org.apache.commons.lang3.ArrayUtils;

import com.hypersocket.realm.Realm;
import com.hypersocket.tasks.Task;
import com.hypersocket.triggers.TaskResult;

public class TemplateTaskResult extends TaskResult {

	public static final String EVENT_RESOURCE_KEY = "<resource>.result";
	
	public TemplateTaskResult(Object source, String resourceKey,
			boolean success, Realm currentRealm, Task task) {
		super(source, resourceKey, success, currentRealm, task);
	}

	public TemplateTaskResult(Object source, String resourceKey, Throwable e,
			Realm currentRealm, Task task) {
		super(source, resourceKey, e, currentRealm, task);
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
