package com.github.maxopoly.WPCommon.model;

public class MCAccount {

	private int id;
	private String name;

	public MCAccount(String name, int id) {
		this.id = id;
		this.name = name;
	}

	public MCAccount(String name) {
		this(name, -1);
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

}
