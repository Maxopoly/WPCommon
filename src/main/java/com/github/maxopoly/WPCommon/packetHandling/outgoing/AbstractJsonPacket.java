package com.github.maxopoly.WPCommon.packetHandling.outgoing;

import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public abstract class AbstractJsonPacket implements IPacket {

	public abstract void setupJSON(JSONObject json);

	@Override
	public byte[] getData() {
		JSONObject msg = new JSONObject();
		setupJSON(msg);
		return msg.toString().getBytes(StandardCharsets.UTF_8);
	}

}
