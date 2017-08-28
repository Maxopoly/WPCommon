package com.github.maxopoly.WPCommon.packetHandling.incoming;

import org.json.JSONObject;

public interface JSONPacketHandler extends IPacketHandler {

	public void handle(JSONObject json);

}
