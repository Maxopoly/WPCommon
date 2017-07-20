package com.github.maxopoly.WPCommon.packetHandling;

import org.json.JSONObject;

public abstract class AbstractPacketHandler {

	private String toHandle;

	public AbstractPacketHandler(String toHandle) {
		this.toHandle = toHandle;
	}

	public String getRequestToHandle() {
		return toHandle;
	}

	public abstract void handle(JSONObject msg);

}
