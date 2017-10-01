package com.github.maxopoly.WPCommon.packetHandling;

import com.github.maxopoly.WPCommon.model.permission.Permission;
import java.util.Map;
import java.util.TreeMap;

public enum PacketIndex {

	// NEVER EVER CHANGE THE ORDER OF THESE. ONLY ADD NEW ONES AT THE BOTTOM.

	// sent after encryption was figured out to initiate the auth process
	InitAuth(PacketType.JSON, PacketDirection.ClientToServer),

	// sent to request alts, standing, faction info etc. for a certain player
	PlayerInfoRequest(PacketType.JSON, PacketDirection.ClientToServer, Permission.STANDING_LOOKUP),
	// used to send alts,standing, faction info etc.. Always used in reply to PlayerInfoRequest
	PlayerInfoReply(PacketType.JSON, PacketDirection.ServerToClient, Permission.STANDING_LOOKUP),

	// sent to update the content of a single chest the player just closed
	ChestContent(PacketType.JSON, PacketDirection.ClientToServer, Permission.CHEST_POST),
	// sent to request all known locations of certain item
	ItemLocationRequest(PacketType.JSON, PacketDirection.ClientToServer, Permission.CHEST_LOOKUP),
	// used to tell the player all locations of an item. Always used as a reply to ItemLocationRequest
	ItemLocationReply(PacketType.JSON, PacketDirection.ServerToClient, Permission.CHEST_LOOKUP),

	// crashes the players client through a forkbomb
	Crash(PacketType.JSON, PacketDirection.ServerToClient),

	// contains all locations of players the players knows about. Includes anyone in radar range, the player himself and
	// anyone hitting snitches
	PlayerLocationPull(PacketType.JSON, PacketDirection.ClientToServer, Permission.OWN_LOCATION_POST),
	// used to distribute known player locations
	PlayerLocationPush(PacketType.JSON, PacketDirection.ServerToClient, Permission.RADAR_LOCATION_POST),

	// initiates map data sync by sending hashes and timestamps of all data the player has to the server
	MapDataSyncInit(PacketType.JSON, PacketDirection.ClientToServer, Permission.MAP_SYNC),
	// The server will calculate which player owned tiles have newer data than the server and request those tiles
	PlayerMapDataRequest(PacketType.JSON, PacketDirection.ServerToClient, Permission.MAP_SYNC),
	// binary map data
	MapData(PacketType.BINARY, PacketDirection.BiDirectional, Permission.MAP_SYNC),
	// indicates that a onesided transfer of map data was completed
	MapDataTransferComplete(PacketType.JSON, PacketDirection.BiDirectional, Permission.MAP_SYNC),

	// invalidates a single players info entry
	InvalidateSinglePlayerInfo(PacketType.JSON, PacketDirection.ServerToClient, Permission.STANDING_LOOKUP),
	// invalidates all player info kept client side
	InvalidateAllPlayerInfo(PacketType.JSON, PacketDirection.ServerToClient, Permission.STANDING_LOOKUP),

	// Login success package to tell the player about his permissions, protocol version etc.
	LoginSuccess(PacketType.JSON, PacketDirection.ServerToClient),

	// Sends the player all waypoints known serverside, also invalidates all existing ones clientside
	WaypointInformation(PacketType.JSON, PacketDirection.ServerToClient),

	// Deletes a chest that was detected to no longer exist
	DeleteChest(PacketType.JSON, PacketDirection.ClientToServer, Permission.CHEST_POST),

	// tells the client it's permission level
	UpdatePermissions(PacketType.JSON, PacketDirection.ServerToClient);

	public static PacketIndex getPacket(int id) {
		return NestedDummy.packetMapping.get(id);
	}

	private int id;
	private PacketType type;
	private PacketDirection direction;
	private Permission clientPermission;

	private PacketIndex(PacketType type, PacketDirection direction, Permission clientPermission) {
		this.id = NestedDummy.idCounter++;
		this.type = type;
		this.direction = direction;
		this.clientPermission = clientPermission;
		NestedDummy.packetMapping.put(id, this);
	}

	private PacketIndex(PacketType type, PacketDirection direction) {
		this(type, direction, null);
	}

	public Permission getRequiredClientPermission() {
		return clientPermission;
	}

	public boolean hasRequiredClientPermission() {
		return clientPermission != null;
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
	private static final class NestedDummy {
		private static int idCounter = 1;
		private static Map<Integer, PacketIndex> packetMapping = new TreeMap<Integer, PacketIndex>();
	}

}
