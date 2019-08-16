package com.hypersocket.template;

import com.hypersocket.permissions.PermissionType;


public enum TemplateResourcePermission implements PermissionType {
	
	READ("read"),
	CREATE("create", READ),
	UPDATE("update", READ),
	DELETE("delete", READ);
	
	private final String val;
	
	private final static String name = "<resource>";
	
	private PermissionType[] implies;
	
	private TemplateResourcePermission(final String val, PermissionType... implies) {
		this.val = name + "." + val;
		this.implies = implies;
	}

	@Override
	public PermissionType[] impliesPermissions() {
		return implies;
	}	
	
	public String toString() {
		return val;
	}

	@Override
	public String getResourceKey() {
		return val;
	}
	
	@Override
	public boolean isSystem() {
		return false;
	}

	@Override
	public boolean isHidden() {
		return false;
	}

}
