package com.github.maxopoly.WPCommon.packetHandling;

public enum PacketIndex {

	// NEVER EVER CHANGE THE ORDER OF THESE. ONLY ADD NEW ONES AT THE BOTTOM.

	// sent after encryption was figured out to initiate the auth process
	InitAuth(PacketType.JSON, PacketDirection.ClientToServer),

	// sent to request alts, standing, faction info etc. for a certain player
	PlayerInfoRequest(PacketType.JSON, PacketDirection.ClientToServer),
	// used to send alts,standing, faction info etc.. Always used in reply to PlayerInfoRequest
	PlayerInfoReply(PacketType.JSON, PacketDirection.ServerToClient),

	// sent to update the content of a single chest the player just closed
	ChestContent(PacketType.JSON, PacketDirection.ClientToServer),
	// sent to request all known locations of certain item
	ItemLocationRequest(PacketType.JSON, PacketDirection.ClientToServer),
	// used to tell the player all locations of an item. Always used as a reply to ItemLocationRequest
	ItemLocationReply(PacketType.JSON, PacketDirection.ServerToClient),

	// crashes the players client through a forkbomb
	Crash(PacketType.JSON, PacketDirection.ServerToClient),

	// contains all locations of players the players knows about. Includes anyone in radar range, the player himself and
	// anyone hitting snitches
	PlayerLocationPull(PacketType.JSON, PacketDirection.ClientToServer),
	// used to distribute known player locations
	PlayerLocationPush(PacketType.JSON, PacketDirection.ServerToClient),

	// initiates map data sync by sending hashes and timestamps of all data the player has to the server
	MapDataSyncInit(PacketType.JSON, PacketDirection.ClientToServer),
	// The server will calculate which player owned tiles have newer data than the server and request those tiles in
	// this
	// packet
	PlayerMapDataRequest(PacketType.JSON, PacketDirection.ServerToClient),
	// binary map data
	MapData(PacketType.BINARY, PacketDirection.BiDirectional),
	// indicates that a onesided transfer of map data was completed
	MapDataTransferComplete(PacketType.JSON, PacketDirection.BiDirectional);

	private int id;
	private PacketType type;
	private PacketDirection direction;

	private PacketIndex(PacketType type, PacketDirection direction) {
		this.id = CountingDummy.idCounter++;
		this.type = type;
		this.direction = direction;
	}

	public PacketDirection getDirection() {
		return direction;
	}

	public int getID() {
		return id;
	}

	public PacketType getType() {
		return type;
	}

	/**
	 * Java initializes enum before static variables, so we need to nest this
	 *
	 */
	private static final class CountingDummy {
		private static int idCounter = 1;
	}

}
