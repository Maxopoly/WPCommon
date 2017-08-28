package com.github.maxopoly.WPCommon.packetHandling.outgoing;

import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public abstract class AbstractJsonPacket implements IPacket {

	private JSONObject msg;

	public abstract void setupJSON(JSONObject json);

	public JSONObject getMessage() {
		return msg;
	}

	@Override
	public byte[] getData() {
		msg = new JSONObject();
		setupJSON(msg);
		return msg.toString().getBytes(StandardCharsets.UTF_8);
	}

}
