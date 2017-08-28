package com.github.maxopoly.WPCommon.packetHandling.handlers;

import com.github.maxopoly.WPCommon.packetHandling.PacketIndex;
import com.github.maxopoly.WPCommon.packetHandling.incoming.JSONPacketHandler;
import org.json.JSONObject;

public abstract class MapDataCompletionPacketHandler implements JSONPacketHandler {

	public PacketIndex getPacketToHandle() {
		return PacketIndex.MapDataTransferComplete;
	}

	public void handle(JSONObject json) {
		handle(json.getInt("id"));
	}

	public abstract void handle(int id);

}
