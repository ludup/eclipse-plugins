package com.hypersocket.assignable;

import com.hypersocket.tables.Column;

public enum TemplateAssignableResourceColumns implements Column {

	NAME;

	public String getColumnName() {
		switch(this.ordinal()) {
		default:
			return "name";
		}
	}
}