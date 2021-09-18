package fr.prodrivers.bukkit.bedrocktexturelib.resource.texture;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public class Atlas {
	private final Map<String, JsonNode> itemAtlas;
	private final Map<String, JsonNode> terrainAtlas;

	public Atlas(Map<String, JsonNode> itemAtlas, Map<String, JsonNode> terrainAtlas) {
		this.itemAtlas = itemAtlas;
		this.terrainAtlas = terrainAtlas;
	}

	public JsonNode getItem(String name) {
		return itemAtlas.get(name);
	}

	public JsonNode getTerrain(String name) {
		return terrainAtlas.get(name);
	}

	public JsonNode get(String name) {
		JsonNode item = getItem(name);
		if(item != null) {
			return item;
		}
		return getTerrain(name);
	}
}
