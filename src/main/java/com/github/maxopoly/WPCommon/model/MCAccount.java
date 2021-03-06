package com.github.maxopoly.WPCommon.model;

public class MCAccount {

	private int id;
	private String name;

	public MCAccount(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public MCAccount(String name) {
		this(-1, name);
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof MCAccount && ((MCAccount) o).name != null && ((MCAccount) o).name.equals(name);
	}

}
