package fr.prodrivers.bukkit.bedrockbridger.resource.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.prodrivers.bukkit.bedrockbridger.Log;

import java.io.File;
import java.io.IOException;

public class ResourcePackManifestParser {
	private final JsonNode root;

	public ResourcePackManifestParser(File resourcePackPath) throws IOException {
		File resourcePackManifestPath = resourcePackPath.toPath().resolve("manifest.json").toFile();
		ObjectMapper objectMapper = new ObjectMapper();
		root = objectMapper.readTree(resourcePackManifestPath);
	}

	public int getFormatVersion() {
		return root.path("format_version").asInt(-1);
	}

	public ResourcePackParser getResourcePackParser() {
		int formatVersion = getFormatVersion();
		if(formatVersion < 0) {
			Log.severe("Invalid manifest, no format version field found.");
			return null;
		}

		if(formatVersion == 2) {
			return new fr.prodrivers.bukkit.bedrockbridger.resource.parser.format.v2.ResourcePackParser();
		}

		Log.severe("Resource pack version not supported : " + formatVersion);
		return null;
	}
}
