package com.github.maxopoly.WPCommon.model;

import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

public class Chest {

	private Location location;
	private Set<WPItem> content;

	public Chest(Location location) {
		this(location, new HashSet<WPItem>());
	}

	public Chest(Location location, Set<WPItem> content) {
		this.location = location;
		this.content = content;
	}

	public Chest(JSONObject json) {
		location = new Location(json.getJSONObject("loc"));
		JSONArray contentArray = json.getJSONArray("content");
		this.content = new HashSet<WPItem>();
		for (int i = 0; i < contentArray.length(); i++) {
			String itemString = contentArray.getString(i);
			WPItem item = new WPItem(itemString);
			this.content.add(item);
		}
	}

	public JSONObject serialize() {
		JSONObject json = new JSONObject();
		json.put("loc", location.serialize());
		JSONArray contentArray = new JSONArray();
		for (WPItem item : this.content) {
			contentArray.put(item.serialize());
		}
		json.put("content", contentArray);
		return json;
	}

	public Location getLocation() {
		return location;
	}

	public Set<WPItem> getContent() {
		return content;
	}

	public void setContent(Set<WPItem> content) {
		this.content = content;
	}

	public int getAmount(WPItem item) {
		for (WPItem chestItem : content) {
			if (chestItem.equals(item)) {
				return chestItem.getAmount();
			}
		}
		return 0;
	}

	public void addItem(WPItem item) {
		boolean found = false;
		for (WPItem chestItem : content) {
			if (chestItem.equals(item)) {
				chestItem.setAmount(item.getAmount() + chestItem.getAmount());
				found = true;
				break;
			}
		}
		if (!found) {
			content.add(item);
		}
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
