package com.github.maxopoly.WPCommon.model;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class Chest {

	private Location location;
	private Map<Integer, Integer> content;

	public Chest(Location location, Map<Integer, Integer> content) {
		this.location = location;
		this.content = content;
	}

	public Chest(JSONObject json) {
		location = new Location(json.getJSONObject("loc"));
		JSONArray contentArray = json.getJSONArray("content");
		this.content = new TreeMap<Integer, Integer>();
		for (int i = 0; i < contentArray.length(); i++) {
			String itemString = contentArray.getString(i);
			String[] split = itemString.split(":");
			this.content.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
		}
	}

	public JSONObject serialize() {
		JSONObject json = new JSONObject();
		json.put("loc", location.serialize());
		JSONArray contentArray = new JSONArray();
		for (Entry<Integer, Integer> entry : this.content.entrySet()) {
			contentArray.put(String.valueOf(entry.getKey()) + ":" + String.valueOf(entry.getValue()));
		}
		json.put("content", contentArray);
		return json;
	}

	public Location getLocation() {
		return location;
	}

	public Map<Integer, Integer> getContent() {
		return content;
	}

	public void setContent(Map<Integer, Integer> content) {
		this.content = content;
	}

	public int getAmount(int id) {
		Integer amnt = content.get(id);
		if (amnt == null) {
			return 0;
		}
		return amnt;
	}

	public void setAmount(int id, int amount) {
		content.put(id, amount);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Chest && ((Chest) o).location.equals(location);
	}

	@Override
	public int hashCode() {
		return location.hashCode();
	}

}
