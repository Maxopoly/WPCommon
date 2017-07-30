package com.github.maxopoly.WPCommon.model;

public class WPItem {

	private static final String sep = ":";

	private int id;
	private int durability;
	private int amount;
	private boolean enchanted;
	private boolean compacted;

	public WPItem(int id) {
		this(id, 1);
	}

	public WPItem(int id, int amount) {
		this(id, amount, 0);
	}

	public WPItem(int id, int amount, int durability) {
		this(id, amount, durability, false, false);
	}

	public WPItem(int id, int amount, int durability, boolean compacted, boolean enchanted) {
		this.id = id;
		this.durability = durability;
		this.compacted = compacted;
		this.amount = amount;
		this.enchanted = enchanted;
	}

	public WPItem(String serial) {
		String[] parts = serial.split(sep);
		amount = Integer.parseInt(parts[0]);
		id = Integer.parseInt(parts[1]);
		if (parts.length >= 3) {
			durability = Integer.parseInt(parts[2]);
		} else {
			durability = 0;
		}
		if (parts.length >= 4) {
			String special = parts[3];
			// no error handling here, we assume it was always encoded by the function below
			if (special.equals("e")) {
				enchanted = true;
				compacted = false;
			} else {
				compacted = true;
				enchanted = false;
			}
		} else {
			enchanted = false;
			compacted = false;
		}
	}

	public int getID() {
		return this.id;
	}

	public int getDurability() {
		return durability;
	}

	public boolean isEnchanted() {
		return enchanted;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean isCompacted() {
		return compacted;
	}

	public String serialize() {
		StringBuilder sb = new StringBuilder();
		sb.append(amount);
		sb.append(sep);
		sb.append(id);
		String special = null;
		if (compacted) {
			special = "c";
		}
		if (enchanted) {
			special = "e";
		}
		if (special != null) {
			sb.append(sep);
			sb.append(durability);
			sb.append(sep);
			sb.append(special);
		} else {
			// only add dura if its not 0 or item is special
			if (durability != 0) {
				sb.append(sep);
				sb.append(durability);
			}
		}
		return sb.toString();
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public boolean isRepairable() {
		// fail fast first
		if (id > 317 || id < 256) {
			return false;
		}
		// dia tools + some other
		if (id >= 267 && id <= 279) {
			return true;
		}
		// iron tools + flint&steel
		if (id >= 256 && id <= 259) {
			return true;
		}
		// bow
		if (id == 261) {
			return true;
		}
		// gold tools
		if (id >= 283 && id <= 286) {
			return true;
		}
		// hoes
		if (id >= 290 && id <= 294) {
			return true;
		}
		if (id >= 298 && id <= 317) {
			return true;
		}
		return false;
	}

	public boolean equalsBase(WPItem i) {
		return i.id == id && i.durability == durability;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof WPItem)) {
			return false;
		}
		WPItem i = (WPItem) o;
		return i.id == id && i.durability == durability && i.compacted == compacted && i.enchanted == enchanted;
	}

	@Override
	public int hashCode() {
		int result = 245521;
		result = 37 * result + id;
		result = 37 * result + durability;
		result = 37 * result + (enchanted ? 0 : 1);
		result = 31 * result + (compacted ? 0 : 1);
		return result;
	}
}
