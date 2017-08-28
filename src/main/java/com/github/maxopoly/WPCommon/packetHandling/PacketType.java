package com.github.maxopoly.WPCommon.packetHandling;

import java.util.Map;
import java.util.TreeMap;

public enum PacketType {
	JSON(1), BINARY(2);

	private Map<Integer, PacketType> byId = new TreeMap<Integer, PacketType>();

	private int id;

	private PacketType(int id) {
		this.id = id;
		byId.put(id, this);
	}

	public int getID() {
		return id;
	}
}
