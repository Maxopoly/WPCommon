package com.github.maxopoly.WPCommon.model;

import org.json.JSONObject;

public class WPMappingTile {
	private static final int emptyColor = 0;

	public static final int length = 512;
	public static final int imgSize = length * length;

	private final CoordPair coords;

	private int[] data;
	private long timeStamp;
	private Integer hash;

	public WPMappingTile(int[] data, long timeStamp, int xPos, int zPos) {
		this.data = data;
		this.timeStamp = timeStamp;
		this.coords = new CoordPair(xPos, zPos);
	}

	public WPMappingTile(long timeStamp, int xPos, int zPos, int hash) {
		this.hash = hash;
		this.timeStamp = timeStamp;
		this.coords = new CoordPair(xPos, zPos);
	}

	public WPMappingTile(CoordPair coords) {
		this.coords = coords;
	}

	public WPMappingTile(JSONObject json) {
		this.hash = json.getInt("hash");
		this.timeStamp = json.getLong("time");
		int x = json.getInt("x");
		int z = json.getInt("z");
		this.coords = new CoordPair(x, z);
	}

	public JSONObject serialize() {
		JSONObject json = new JSONObject();
		json.put("x", coords.getX());
		json.put("z", coords.getZ());
		json.put("time", timeStamp);
		if (hash != null) {
			json.put("hash", hash);
		}
		return json;
	}

	public String getFileName() {
		return String.format("%d,%d.png", coords.getX(), coords.getZ());
	}

	public WPMappingTile merge(WPMappingTile other) {
		if (!other.coords.equals(coords)) {
			throw new IllegalArgumentException("Can only merge tiles at the same location");
		}
		int[] newData = new int[imgSize];
		WPMappingTile older, newer;
		if (timeStamp > other.timeStamp) {
			newer = this;
			older = other;
		} else {
			older = this;
			newer = other;
		}
		int[] newerData = newer.getDataDump();
		int[] olderData = older.getDataDump();
		for (int i = 0; i < imgSize; i++) {
			if (newerData[i] == emptyColor) {
				newData[i] = olderData[i];
			} else {
				newData[i] = newerData[i];
			}
		}
		return new WPMappingTile(newData, newer.timeStamp, coords.getX(), coords.getZ());
	}

	public CoordPair getCoords() {
		return coords;
	}

	public int getHash() {
		if (hash == null) {
			calcHash();
		}
		return hash;
	}

	public void updateTimeStampAndHash(long timeStamp, int hash) {
		this.timeStamp = timeStamp;
		this.hash = hash;
	}

	private void calcHash() {
		int result = 244523;
		for (int i : data) {
			result = 37 * result + i;
		}
		hash = result;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public int[] getDataDump() {
		return data;
	}

	@Override
	public int hashCode() {
		return coords.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof WPMappingTile)) {
			return false;
		}
		return coords.equals(((WPMappingTile) o).coords);
	}
}
