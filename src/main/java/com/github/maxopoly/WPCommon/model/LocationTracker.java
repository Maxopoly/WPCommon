package com.github.maxopoly.WPCommon.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocationTracker {

	// instance
	private static LocationTracker instance;

	private Map<String, LoggedPlayerLocation> locations;

	private Set<String> recentlyUpdated;

	private long snitchOverWriteThreshhold = 2000;

	private LocationTracker() {
		locations = new HashMap<String, LoggedPlayerLocation>();
		recentlyUpdated = new HashSet<String>();
	}

	public static LocationTracker getInstance() {
		if (instance == null) {
			instance = new LocationTracker();
		}
		return instance;
	}

	public synchronized List<String> pullAndClearRecentlyUpdatedPlayers() {
		List<String> copy = new LinkedList<String>(recentlyUpdated);
		recentlyUpdated.clear();
		return copy;
	}

	public synchronized void reportSnitchLocation(String name, Location location) {
		// this needs to be separate from normal location reporting, because we dont want snitch hits, which just report the
		// location of the snitch, to overwrite radar based data
		LoggedPlayerLocation previous = locations.get(name);
		LoggedPlayerLocation replacement = new LoggedPlayerLocation(location, name, LocationType.SNITCH);
		if (previous == null) {
			// not tracked so far
			reportLocation(replacement);
			return;
		}
		if (replacement.getTimeStamp() - previous.getTimeStamp() > snitchOverWriteThreshhold) {
			reportLocation(replacement);
		}
	}

	public synchronized void reportRadarLocation(String name, Location location) {
		reportLocation(new LoggedPlayerLocation(location, name, LocationType.RADAR));
	}

	public synchronized void reportLocally(LoggedPlayerLocation loc) {
		LoggedPlayerLocation existing = locations.get(loc.getPlayer());
		if (existing != null && existing.getTimeStamp() > loc.getTimeStamp()) {
			// newer data already exists
			return;
		}

		locations.put(loc.getPlayer(), loc);
	}

	public synchronized void reportLocation(LoggedPlayerLocation loc) {
		LoggedPlayerLocation existing = locations.get(loc.getPlayer());
		if (existing != null && existing.getTimeStamp() > loc.getTimeStamp()) {
			// newer data already exists
			return;
		}
		recentlyUpdated.add(loc.getPlayer());
		locations.put(loc.getPlayer(), loc);
	}

	public synchronized LoggedPlayerLocation getLastKnownLocation(String player) {
		return locations.get(player);
	}

}
