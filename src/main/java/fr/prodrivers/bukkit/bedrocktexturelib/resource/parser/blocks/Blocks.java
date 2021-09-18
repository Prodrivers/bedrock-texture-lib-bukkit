package fr.prodrivers.bukkit.bedrocktexturelib.resource.parser.blocks;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.prodrivers.bukkit.bedrocktexturelib.Log;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.texture.Atlas;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.texture.TerrainTexture;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Blocks {
	protected final JsonNode root;
	protected final Atlas atlas;

	protected Blocks(File filePath, Atlas atlas) throws IOException {
		ObjectMapper objectMapper = (new ObjectMapper()).configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		this.root = objectMapper.readTree(filePath);
		this.atlas = atlas;
	}

	public Integer[] getFormatVersion() {
		List<Integer> version = new ArrayList<>();
		root.path("format_version").elements().forEachRemaining(node -> {
			if(node.isInt()) {
				version.add(node.asInt());
			}
		});
		return version.size() == 0 ? null : version.toArray(new Integer[]{});
	}

	public Collection<Map.Entry<String, String>> getAllTextureNames(String name) {
		throw new UnsupportedOperationException();
	}

	private String getTexturePathForTextureName(String name, byte blockData) {
		JsonNode node = atlas.getTerrain(name);
		if(node == null) {
			return null;
		}
		return (new TerrainTexture(node)).getTexturePath(blockData);
	}

	public String getBestTextureName(String name) {
		throw new UnsupportedOperationException();
	}

	public Collection<String> getAllTextures(String name, byte blockData) {
		return getAllTextureNames(name).stream()
				.map(nameEntry -> getTexturePathForTextureName(nameEntry.getValue(), blockData))
				.filter(Objects::isNull)
				.collect(Collectors.toList());
	}

	public String getBestTexture(String name, byte blockData) {
		String textureName = getBestTextureName(name);
		return textureName != null ? getTexturePathForTextureName(textureName, blockData) : null;
	}

	public static Blocks getParser(File filePath, Atlas atlas) throws IOException {
		Blocks baseParser = new Blocks(filePath, atlas);
		Integer[] formatVersion = baseParser.getFormatVersion();
		if(formatVersion == null) {
			Log.severe("Invalid manifest, no format version field found for blocks.");
			return null;
		}

		if(formatVersion.length == 3 && formatVersion[0] == 1 && formatVersion[1] == 1) {
			return new fr.prodrivers.bukkit.bedrocktexturelib.resource.parser.blocks.format.v1_1_0.Blocks(filePath, atlas);
		}

		Log.severe("Blocks version not supported : " + Arrays.toString(formatVersion));
		return null;
	}
}
