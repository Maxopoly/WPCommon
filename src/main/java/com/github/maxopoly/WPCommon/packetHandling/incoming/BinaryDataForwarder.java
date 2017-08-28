package com.github.maxopoly.WPCommon.packetHandling.incoming;

import com.github.maxopoly.WPCommon.packetHandling.PacketType;
import org.apache.logging.log4j.Logger;

public class BinaryDataForwarder extends GenericDataForwarder<BinaryPacketHandler> {

	public BinaryDataForwarder(Logger logger) {
		super(logger);
	}

	@Override
	public void handle(byte[] data, BinaryPacketHandler subHandler) {
		subHandler.handle(data);
	}

	@Override
	public PacketType getTypeToHandle() {
		return PacketType.BINARY;
	}

}
