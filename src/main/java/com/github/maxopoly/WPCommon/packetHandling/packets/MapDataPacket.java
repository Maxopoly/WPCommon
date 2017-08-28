package com.github.maxopoly.WPCommon.packetHandling.packets;

import com.github.maxopoly.WPCommon.model.WPMappingTile;
import com.github.maxopoly.WPCommon.packetHandling.PacketIndex;
import com.github.maxopoly.WPCommon.packetHandling.outgoing.IPacket;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MapDataPacket implements IPacket {

	private WPMappingTile tile;
	private int session;

	public MapDataPacket(WPMappingTile tile, int session) {
		this.tile = tile;
		this.session = session;
	}

	@Override
	public PacketIndex getPacket() {
		return PacketIndex.MapData;
	}

	@Override
	public byte[] getData() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(stream);
		int[] data = tile.getDataDump();
		try {
			dataStream.writeInt(session);
			dataStream.writeInt(tile.getCoords().getX());
			dataStream.writeInt(tile.getCoords().getZ());
			dataStream.writeLong(tile.getTimeStamp());
			for (int i = 0; i < WPMappingTile.imgSize; i++) {
				dataStream.writeInt(data[i]);
			}
		} catch (IOException e) {
			return null;
		}
		return stream.toByteArray();
	}

}
