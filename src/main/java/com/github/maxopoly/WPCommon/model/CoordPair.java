package com.github.maxopoly.WPCommon.model;

import org.json.JSONObject;

public class CoordPair {

	private int x;
	private int z;

	public CoordPair(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public JSONObject serialize() {
		JSONObject json = new JSONObject();
		json.put("x", x);
		json.put("z", z);
		return json;
	}

	@Override
	public int hashCode() {
		int result = 2352;
		result = 37 * result + x;
		result = 31 * result + z;
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CoordPair)) {
			return false;
		}
		CoordPair other = (CoordPair) o;
		return other.x == x && other.z == z;
	}

	@Override
	public String toString() {
		return "(" + x + "," + z + ")";
	}

}
