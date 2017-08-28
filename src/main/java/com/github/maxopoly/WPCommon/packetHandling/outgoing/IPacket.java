package com.github.maxopoly.WPCommon.packetHandling.outgoing;

import com.github.maxopoly.WPCommon.packetHandling.PacketIndex;

public interface IPacket {

	public PacketIndex getPacket();

	public byte[] getData();

}
