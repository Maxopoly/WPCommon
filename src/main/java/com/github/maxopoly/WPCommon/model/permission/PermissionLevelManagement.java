package com.github.maxopoly.WPCommon.model.permission;

import java.util.Map;
import java.util.TreeMap;

public class PermissionLevelManagement {

	private static Map<Integer, PermissionLevel> levels;

	static {
		levels = new TreeMap<Integer, PermissionLevel>();
		// everything, trusted members
		levels.put(1, new PermissionLevel(1, Permission.values()));
		// everything except chests, normal members
		levels.put(2, new PermissionLevel(2, Permission.ALT_LOOKUP, Permission.STANDING_LOOKUP,
				Permission.OWN_LOCATION_POST, Permission.RADAR_LOCATION_POST, Permission.SNITCH_LOCATION_POST,
				Permission.RADAR_LOCATION_READ, Permission.SNITCH_LOCATION_READ, Permission.MAP_SYNC,
				Permission.SECRET_WAYPOINTS, Permission.TACTICAL_WAYPOINTS, Permission.AUTH));
		// trusted fighers/foreigners
		levels.put(2, new PermissionLevel(2, Permission.ALT_LOOKUP, Permission.STANDING_LOOKUP,
				Permission.OWN_LOCATION_POST, Permission.RADAR_LOCATION_POST, Permission.SNITCH_LOCATION_POST,
				Permission.RADAR_LOCATION_READ, Permission.SNITCH_LOCATION_READ, Permission.MAP_SYNC,
				Permission.TACTICAL_WAYPOINTS, Permission.AUTH));
		// only radar, random fighters
		levels.put(3, new PermissionLevel(3, Permission.STANDING_LOOKUP, Permission.OWN_LOCATION_POST,
				Permission.RADAR_LOCATION_POST, Permission.SNITCH_LOCATION_POST, Permission.RADAR_LOCATION_READ,
				Permission.TACTICAL_WAYPOINTS, Permission.AUTH));
		// anyone unknown
		levels.put(4, new PermissionLevel(4, Permission.OWN_LOCATION_POST, Permission.RADAR_LOCATION_POST,
				Permission.SNITCH_LOCATION_POST, Permission.AUTH));
		// not authed yet
		levels.put(5, new PermissionLevel(5, Permission.AUTH));
		// blacklisted
		levels.put(6, new PermissionLevel(6));
	}

	public static PermissionLevel getPermissionLevel(int level) {
		return levels.get(level);
	}

	public static PermissionLevel getDefaultPermissionLevel() {
		return levels.get(4);
	}

	public static PermissionLevel getAuthOnlyPermissionLevel() {
		return levels.get(5);
	}
}
