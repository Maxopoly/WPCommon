package com.github.maxopoly.WPCommon.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Player {

	private Faction faction;
	private List<String> accounts;
	private String main;
	private int standing;

	public Player(Faction faction, List<String> accounts, String main, int standing) {
		if (main == null) {
			throw new IllegalArgumentException("Main must not be null");
		}
		this.faction = faction;
		this.accounts = accounts;
		this.main = main;
		this.standing = standing;
	}

	public Player(JSONObject json) {
		JSONObject factionJson = json.optJSONObject("faction");
		if (factionJson != null) {
			this.faction = new Faction(factionJson);
		}
		this.main = json.getString("main");
		JSONArray altArray = json.getJSONArray("alts");
		accounts = new LinkedList<String>();
		for (int i = 0; i < altArray.length(); i++) {
			accounts.add(altArray.getString(i));
		}
		this.standing = json.getInt("standing");
	}

	public Faction getFaction() {
		return faction;
	}

	public List<String> getAccounts() {
		return Collections.unmodifiableList(accounts);
	}

	public String getMain() {
		return main;
	}

	public int getStanding() {
		return standing;
	}

	public JSONObject serialize() {
		JSONObject json = new JSONObject();
		json.put("main", main);
		json.put("alts", accounts);
		json.put("standing", standing);
		if (faction != null) {
			json.put("faction", faction.serialize());
		}
		return json;
	}
}
