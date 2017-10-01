package com.github.maxopoly.WPCommon.model;

import com.github.maxopoly.WPCommon.model.permission.Permission;

public enum LocationType {
	RADAR, SNITCH, PPBROADCAST;

	public Permission getMatchingReadPermission() {
		switch (this) {
			case RADAR:
				return Permission.RADAR_LOCATION_READ;
			case SNITCH:
				return Permission.SNITCH_LOCATION_READ;
			case PPBROADCAST:
			default:
				return Permission.RADAR_LOCATION_READ;

		}
	}
}
