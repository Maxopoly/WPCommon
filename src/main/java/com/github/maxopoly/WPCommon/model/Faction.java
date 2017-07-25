package com.github.maxopoly.WPCommon.model;

import org.json.JSONObject;

public class Faction {

	private String name;
	private int standing;
	private int id;

	public Faction(int id, String name, int standing) {
		this.id = id;
		this.name = name;
		this.standing = standing;
	}

	public Faction(String name, int standing) {
		this(-1, name, standing);
	}

	public Faction(JSONObject json) {
		this.name = json.getString("name");
		this.standing = json.getInt("standing");
	}

	public String getName() {
		return name;
	}

	public int getStanding() {
		return standing;
	}

	public int getID() {
		return id;
	}

	public JSONObject serialize() {
		JSONObject factionJson = new JSONObject();
		factionJson.put("name", name);
		factionJson.put("standing", standing);
		return factionJson;
	}
}
