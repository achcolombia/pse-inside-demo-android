package com.browser2app.pse.pseinsidedemo;

public class Authorizer {
	private String id;
	private String name;

	public Authorizer(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
