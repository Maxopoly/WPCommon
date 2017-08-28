package com.github.maxopoly.WPCommon.util;

import com.github.maxopoly.WPCommon.model.CoordPair;
import com.github.maxopoly.WPCommon.model.WPMappingTile;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class MapDataFileHandler {

	protected final Pattern regionPicRegex = Pattern.compile("(-?\\d+),(-?\\d+)\\.png");

	private File mappingDataFolder;
	private File dayDataFolder;
	private File hashCacheFile;
	protected Logger logger;

	public MapDataFileHandler(Logger logger) {
		this.logger = logger;
	}

	public WPMappingTile loadMapTile(File f) {
		Matcher m = regionPicRegex.matcher(f.getName());
		if (m.matches()) {
			int x = Integer.parseInt(m.group(1));
			int z = Integer.parseInt(m.group(2));
			BufferedImage img;
			try {
				img = ImageIO.read(f);
			} catch (IOException e) {
				logger.error("Failed to load JM img file", e);
				return null;
			}
			return new WPMappingTile(img.getRGB(0, 0, WPMappingTile.length, WPMappingTile.length, null, 0,
					WPMappingTile.length), f.lastModified(), x, z);
		} else {
			logger.info("Tried to load file " + f.getName() + ", but did not match regex");
		}
		return null;
	}

	public WPMappingTile loadMapTile(CoordPair coords) {
		return loadMapTile(new File(getDayDataFolder(), new WPMappingTile(coords).getFileName()));
	}

	public void saveTile(WPMappingTile tile) {
		File saveFile = new File(getDayDataFolder(), tile.getFileName());
		BufferedImage img = new BufferedImage(WPMappingTile.length, WPMappingTile.length, BufferedImage.TYPE_4BYTE_ABGR);
		img.setRGB(0, 0, WPMappingTile.length, WPMappingTile.length, tile.getDataDump(), 0, WPMappingTile.length);
		try {
			ImageIO.write(img, "png", saveFile);
			saveFile.setLastModified(tile.getTimeStamp());
		} catch (IOException e) {
			logger.error("Failed to save map data img", e);
		}
	}

	public Map<CoordPair, WPMappingTile> loadCachedTileHashes() {
		File file = getTileHashFile();
		Map<CoordPair, WPMappingTile> mapping = new HashMap<CoordPair, WPMappingTile>();
		if (!file.exists()) {
			return buildCachedTileHashMapping();
		}
		StringBuilder sb = new StringBuilder();
		try (BufferedReader buff = new BufferedReader(new FileReader(file));) {
			for (String line = buff.readLine(); line != null; line = buff.readLine()) {
				sb.append(line);
			}
		} catch (FileNotFoundException e) {
			logger.error("Hash file was deleted while loading?", e);
			return mapping;
		} catch (IOException e) {
			logger.error("IOError while loading cached hashed", e);
			return mapping;
		}
		JSONObject json = new JSONObject(sb.toString());
		JSONArray hashArray = json.optJSONArray("hash");
		if (hashArray == null) {
			logger.info("Hash file was empty, no hashes loaded");
		}
		for (int i = 0; i < hashArray.length(); i++) {
			WPMappingTile tile = new WPMappingTile(hashArray.getJSONObject(i));
			mapping.put(tile.getCoords(), tile);
		}

		// validate that we have a complete mapping for all existing files and dont have files in the hash cache that
		// dont actually exist. Also recheck timestamps.
		Map<CoordPair, WPMappingTile> copy = new HashMap<CoordPair, WPMappingTile>(mapping);
		File folder = getDayDataFolder();
		File[] pics = folder.listFiles();
		for (File f : pics) {
			Matcher match = regionPicRegex.matcher(f.getName());
			if (!match.matches()) {
				continue;
			}
			CoordPair coords = new CoordPair(Integer.parseInt(match.group(1)), Integer.parseInt(match.group(2)));
			WPMappingTile cached = copy.get(coords);
			if (cached == null || cached.getTimeStamp() != f.lastModified()) {
				WPMappingTile tile = loadMapTile(f);
				if (tile == null) {
					logger.error("File " + f.getName() + " existed and matched, but couldnt be loaded");
					continue;
				}
				// we dont want to save the actual map data in memory, only meta data
				mapping.put(tile.getCoords(), new WPMappingTile(tile.getTimeStamp(), tile.getCoords().getX(), tile
						.getCoords().getZ(), tile.getHash()));
			}
			copy.remove(coords);
		}
		// all entries left over in the copy at this point dont actually exist as file, so clear them
		for (CoordPair coords : copy.keySet()) {
			logger.info("File for coords " + coords.toString() + " was in cache, but didnt actually exist, fixing...");
			mapping.remove(coords);
		}
		logger.info("Loaded hash cache containing " + mapping.size() + " entries");
		return mapping;
	}

	public Map<CoordPair, WPMappingTile> buildCachedTileHashMapping() {
		logger.info("Building new hash cache mapping");
		Map<CoordPair, WPMappingTile> mapping = new HashMap<CoordPair, WPMappingTile>();
		File folder = getDayDataFolder();
		File[] pics = folder.listFiles();
		for (File f : pics) {
			WPMappingTile tile = loadMapTile(f);
			if (tile == null) {
				continue;
			}
			// we dont want to save the actual map data in memory, only meta data
			mapping.put(tile.getCoords(), new WPMappingTile(tile.getTimeStamp(), tile.getCoords().getX(), tile
					.getCoords().getZ(), tile.getHash()));
		}
		logger.info("Built hash cache containing " + mapping.size() + " entries");
		return mapping;
	}

	public void saveCachedTileHashes(Map<CoordPair, WPMappingTile> tiles) {
		logger.info("Saving data on " + tiles.size() + " tiles to cache file");
		JSONObject json = new JSONObject();
		JSONArray hashArray = new JSONArray();
		for (WPMappingTile tile : tiles.values()) {
			hashArray.put(tile.serialize());
		}
		json.put("hash", hashArray);
		try (BufferedWriter writer = new BufferedWriter(new PrintWriter(getTileHashFile()))) {
			writer.write(json.toString());
		} catch (IOException e) {
			logger.error("Could not save tile hashes", e);
		}
	}

	private File getTileHashFile() {
		if (hashCacheFile == null) {
			File dataFolder = getDayDataFolder();
			File dayFolder = new File(dataFolder, "cachedHashes.wp");
			hashCacheFile = dayFolder;
		}
		return hashCacheFile;
	}

	public abstract File getBaseDirectory();

	public abstract String getMapDataPath();

	public File getMapDataFolder() {
		if (mappingDataFolder == null) {
			File dataFolder = getBaseDirectory();
			File journeyMapFolder = new File(dataFolder.getAbsolutePath() + File.separator + getMapDataPath());
			journeyMapFolder.mkdirs();
			mappingDataFolder = journeyMapFolder;
		}

		return mappingDataFolder;
	}

	public File getDayDataFolder() {
		if (dayDataFolder == null) {
			File dataFolder = getMapDataFolder();
			File dayFolder = new File(dataFolder.getAbsolutePath() + File.separator + "DIM0" + File.separator + "day");
			dayFolder.mkdirs();
			dayDataFolder = dayFolder;
		}
		return dayDataFolder;
	}
}
