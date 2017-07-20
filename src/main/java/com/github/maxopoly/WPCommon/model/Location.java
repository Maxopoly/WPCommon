package com.github.maxopoly.WPCommon.model;

import org.json.JSONObject;

public class Location {

	private double x;
	private double y;
	private double z;

	public Location(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Location(JSONObject json) {
		if (json.has("x") && json.has("y") && json.has("z")) {
			this.x = json.getDouble("x");
			this.y = json.getDouble("y");
			this.z = json.getDouble("z");
		} else {
			throw new IllegalArgumentException("Got invalid json: " + json.toString());
		}
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public int distance(Location loc) {
		return (int) Math.sqrt(Math.pow(x - loc.x, 2) + Math.pow(y - loc.y, 2) + Math.pow(z - loc.z, 2));
	}

	public JSONObject serialize() {
		JSONObject json = new JSONObject();
		json.put("x", x);
		json.put("y", y);
		json.put("z", z);
		return json;
	}

}
