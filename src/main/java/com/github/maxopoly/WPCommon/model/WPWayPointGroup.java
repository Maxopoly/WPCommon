package com.github.maxopoly.WPCommon.model;

import com.github.maxopoly.WPCommon.model.permission.Permission;

public enum WPWayPointGroup {

	CITIES, TACTICAL, MISC, FARMS;

	public Permission getRequiredPermission() {
		switch (this) {
			case CITIES:
			case TACTICAL:
				return Permission.TACTICAL_WAYPOINTS;
			case MISC:
			case FARMS:
			default:
				return Permission.SECRET_WAYPOINTS;

		}
	}

}
