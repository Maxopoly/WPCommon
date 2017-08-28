package com.github.maxopoly.WPCommon.packetHandling.packets;

import com.github.maxopoly.WPCommon.packetHandling.PacketIndex;
import com.github.maxopoly.WPCommon.packetHandling.outgoing.AbstractJsonPacket;
import org.json.JSONObject;

/**
 * Sent to indicate that the transfer of map data is complete
 *
 */
public class MapDataCompletionPacket extends AbstractJsonPacket {

	private int session;

	public MapDataCompletionPacket(int session) {
		this.session = session;
	}

	@Override
	public void setupJSON(JSONObject json) {
		json.put("id", session);
	}

	public PacketIndex getPacket() {
		return PacketIndex.MapDataTransferComplete;
	}

}
