package com.github.maxopoly.WPCommon.packetHandling.incoming;

public interface BinaryPacketHandler extends IPacketHandler {

	public void handle(byte[] data);

}
