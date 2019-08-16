package com.hypersocket.template;

import com.hypersocket.tables.Column;

public enum TemplateResourceColumns implements Column {

	NAME;
	
	/**
	 * TODO Add any additional columns you need to display in the resource table.
	 */
	public String getColumnName() {
		switch(this.ordinal()) {
		default:
			return "name";
		}
	}
}