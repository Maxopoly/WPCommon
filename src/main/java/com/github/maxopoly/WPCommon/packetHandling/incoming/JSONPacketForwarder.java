package com.github.maxopoly.WPCommon.packetHandling.incoming;

import com.github.maxopoly.WPCommon.packetHandling.PacketType;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class JSONPacketForwarder extends GenericDataForwarder<JSONPacketHandler> {

	public JSONPacketForwarder(Logger logger) {
		super(logger);
	}

	@Override
	public PacketType getTypeToHandle() {
		return PacketType.JSON;
	}

	@Override
	public void handle(byte[] data, JSONPacketHandler subHandler) {
		subHandler.handle(new JSONObject(new String(data, StandardCharsets.UTF_8)));
	}

}
