package com.github.maxopoly.WPCommon.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Player {

	private Faction faction;
	private List<MCAccount> accounts;
	private MCAccount main;
	private int standing;
	private int altGroupId;

	public Player(Faction faction, List<MCAccount> accounts, MCAccount main, int standing, int altGroupId) {
		this.faction = faction;
		this.accounts = accounts;
		this.main = main;
		this.standing = standing;
	}

	public Player(Faction faction, List<MCAccount> accounts, MCAccount main, int standing) {
		this(faction, accounts, main, standing, -1);
	}

	public Player(JSONObject json) {
		JSONObject factionJson = json.optJSONObject("faction");
		if (factionJson != null) {
			this.faction = new Faction(factionJson);
		}
		this.main = new MCAccount(json.getString("main"));
		JSONArray altArray = json.getJSONArray("alts");
		accounts = new LinkedList<MCAccount>();
		for (int i = 0; i < altArray.length(); i++) {
			MCAccount alt = new MCAccount(altArray.getString(i));
			accounts.add(alt);
		}
		this.standing = json.getInt("standing");
	}

	public synchronized void addAlt(MCAccount alt) {
		accounts.add(alt);
	}

	public Faction getFaction() {
		return faction;
	}

	public synchronized List<MCAccount> getAccounts() {
		return Collections.unmodifiableList(accounts);
	}

	public MCAccount getMain() {
		return main;
	}

	public int getAltGroupId() {
		return altGroupId;
	}

	public int getTransitiveStanding() {
		if (standing != 0) {
			return standing;
		}
		if (faction != null) {
			return faction.getStanding();
		}
		return 0;
	}

	public int getStanding() {
		return standing;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	public void setStanding(int standing) {
		if (standing > 10 || standing < -10) {
			throw new IllegalArgumentException("Standing must be within 10 and -10");
		}
		this.standing = standing;
	}

	public synchronized JSONObject serialize() {
		JSONObject json = new JSONObject();
		json.put("main", main.getName());
		JSONArray altArrays = new JSONArray();
		for (MCAccount alt : accounts) {
			altArrays.put(alt.getName());
		}
		json.put("alts", altArrays);
		json.put("standing", standing);
		if (faction != null) {
			json.put("faction", faction.serialize());
		}
		return json;
	}
}
