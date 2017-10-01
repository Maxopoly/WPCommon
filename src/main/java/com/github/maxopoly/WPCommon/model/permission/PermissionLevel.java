package com.github.maxopoly.WPCommon.model.permission;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PermissionLevel {

	private Set<Permission> perms;
	private int level;

	PermissionLevel(int id, Permission... perms) {
		this.perms = new HashSet<Permission>(Arrays.asList(perms));
		this.level = id;
	}

	public boolean hasPermission(Permission perm) {
		return perms.contains(perm);
	}

	public int getID() {
		return level;
	}

}
