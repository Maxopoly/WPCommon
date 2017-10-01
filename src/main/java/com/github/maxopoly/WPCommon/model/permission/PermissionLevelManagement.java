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
				Permission.SECRET_WAYPOINTS, Permission.TACTICAL_WAYPOINTS));
		// only radar, random fighters
		levels.put(3, new PermissionLevel(3, Permission.STANDING_LOOKUP, Permission.OWN_LOCATION_POST,
				Permission.RADAR_LOCATION_POST, Permission.SNITCH_LOCATION_POST, Permission.RADAR_LOCATION_READ,
				Permission.TACTICAL_WAYPOINTS));
		// anyone unauthorized
		levels.put(4, new PermissionLevel(4, Permission.OWN_LOCATION_POST, Permission.RADAR_LOCATION_POST,
				Permission.SNITCH_LOCATION_POST));
		// blacklisted
		levels.put(5, new PermissionLevel(5));
	}

	public static PermissionLevel getPermissionLevel(int level) {
		return levels.get(level);
	}
}
