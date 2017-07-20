package com.github.maxopoly.WPCommon.packetHandling;

import org.json.JSONObject;

public abstract class AbstractJsonPacket {

	// sub classes should fill this object in their constructor
	protected JSONObject msg;

	protected AbstractJsonPacket(String request) {
		msg = new JSONObject();
		msg.put("request", request);
	}

	public JSONObject getMessage() {
		return msg;
	}

}
