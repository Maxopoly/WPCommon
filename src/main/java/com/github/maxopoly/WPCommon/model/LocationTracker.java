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

	private Map<String, Location> locations;
	private Map<String, Long> lastUpdated;

	private Set<String> recentlyUpdated;

	private long snitchOverWriteThreshhold = 2000;

	private LocationTracker() {
		locations = new HashMap<String, Location>();
		lastUpdated = new HashMap<String, Long>();
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

	public synchronized void reportSnitch(String name, Location location) {
		// this needs to be separate from normal location reporting, because we dont want snitch hits, which just report the
		// location of the snitch, to overwrite radar based data
		Long time = lastUpdated.get(name);
		if (time == null) {
			// not tracked so far
			reportLocation(name, location);
			return;
		}
		long current = System.currentTimeMillis();
		if (current - time > snitchOverWriteThreshhold) {
			reportLocation(name, location);
		}
	}

	public synchronized void reportLocally(String name, Location location) {
		locations.put(name, location);
		lastUpdated.put(name, System.currentTimeMillis());
	}

	public synchronized void reportLocation(String name, Location location) {
		recentlyUpdated.add(name);
		reportLocally(name, location);
	}

	public synchronized Location getLastKnownLocation(String player) {
		return locations.get(player);
	}

	public synchronized long getMillisSinceLastReport(String player) {
		Long time = lastUpdated.get(player);
		if (time == null) {
			// not tracked so far
			return -1;
		}
		return System.currentTimeMillis() - time;
	}

}
