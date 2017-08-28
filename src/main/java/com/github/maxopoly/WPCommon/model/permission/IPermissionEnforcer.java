package com.github.maxopoly.WPCommon.model.permission;

import com.github.maxopoly.WPCommon.model.Location;

public interface IPermissionEnforcer {

	public boolean isAllowed(String name, Location loc);
}
