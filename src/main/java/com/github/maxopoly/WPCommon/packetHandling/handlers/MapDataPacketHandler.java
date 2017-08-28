package com.github.maxopoly.WPCommon.packetHandling.handlers;

import com.github.maxopoly.WPCommon.model.WPMappingTile;
import com.github.maxopoly.WPCommon.packetHandling.PacketIndex;
import com.github.maxopoly.WPCommon.packetHandling.incoming.BinaryPacketHandler;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.apache.logging.log4j.Logger;

public abstract class MapDataPacketHandler implements BinaryPacketHandler {

	protected Logger logger;

	public MapDataPacketHandler(Logger logger) {
		this.logger = logger;
	}

	public PacketIndex getPacketToHandle() {
		return PacketIndex.MapData;
	}

	public void handle(byte[] data) {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		DataInputStream inStream = new DataInputStream(byteStream);
		try {
			int sessionID = inStream.readInt();
			int xPos = inStream.readInt();
			int zPos = inStream.readInt();
			long timeStamp = inStream.readLong();

			int[] mapData = new int[WPMappingTile.imgSize];
			for (int i = 0; i < mapData.length; i++) {
				mapData[i] = inStream.readInt();
			}
			handle(new WPMappingTile(mapData, timeStamp, xPos, zPos), sessionID);
		} catch (IOException e) {
			logger.error("IOException while trying to read image. Image was ignored", e);
		}

	}

	public abstract void handle(WPMappingTile tile, int sessionID);

}
