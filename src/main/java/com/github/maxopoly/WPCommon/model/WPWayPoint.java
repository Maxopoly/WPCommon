package com.github.maxopoly.WPCommon.model;

import org.json.JSONObject;

public class WPWayPoint {

	private final int color;
	private final Location location;
	private final String name;
	private final WPWayPointGroup group;

	public WPWayPoint(Location location, String name, int color, WPWayPointGroup group) {
		this.color = color;
		this.location = location;
		this.name = name;
		this.group = group;
	}

	public WPWayPoint(JSONObject json) {
		this.color = json.getInt("color");
		this.name = json.getString("name");
		this.location = new Location(json.getJSONObject("loc"));
		this.group = WPWayPointGroup.valueOf(json.getString("group"));
	}

	public JSONObject serialize() {
		JSONObject json = new JSONObject();
		json.put("loc", location.serialize());
		json.put("name", name);
		json.put("group", group.name());
		json.put("color", color);
		return json;
	}

	public int getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	public WPWayPointGroup getGroup() {
		return group;
	}

	public Location getLocation() {
		return location;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof WPWayPoint && ((WPWayPoint) o).location.equals(location);
	}

	@Override
	public int hashCode() {
		return location.hashCode();
	}
}
