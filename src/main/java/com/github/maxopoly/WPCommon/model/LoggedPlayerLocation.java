package com.github.maxopoly.WPCommon.model;

import org.json.JSONObject;

public class LoggedPlayerLocation {

	private LocationType type;
	private Location location;
	private String player;
	private long timeStamp;

	public LoggedPlayerLocation(Location location, String player, LocationType type) {
		this.type = type;
		this.location = location;
		this.player = player;
		this.timeStamp = System.currentTimeMillis();
	}

	public LoggedPlayerLocation(JSONObject json) {
		player = json.getString("name");
		location = new Location(json.getJSONObject("loc"));
		type = LocationType.valueOf(json.getString("type"));
		timeStamp = json.getLong("time");
	}

	public LocationType getType() {
		return type;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public String getPlayer() {
		return player;
	}

	public Location getLocation() {
		return location;
	}

	public JSONObject serialize() {
		JSONObject json = new JSONObject();
		json.put("name", player);
		json.put("loc", location.serialize());
		json.put("type", type.toString());
		json.put("time", timeStamp);
		return json;
	}

}
