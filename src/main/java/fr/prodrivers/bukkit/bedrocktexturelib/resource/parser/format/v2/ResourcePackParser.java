package fr.prodrivers.bukkit.bedrocktexturelib.resource.parser.format.v2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.parser.blocks.Blocks;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.texture.Atlas;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ResourcePackParser implements fr.prodrivers.bukkit.bedrocktexturelib.resource.parser.ResourcePackParser {
	private static final String BLOCKS_FILE = "blocks.json";
	private static final String ITEM_TEXTURE_FILE = "textures/item_texture.json";
	private static final String TERRAIN_TEXTURE_FILE = "textures/terrain_texture.json";

	private Path folderPath;

	@Getter
	private Atlas atlas;

	@Getter
	private Blocks blocks;

	@Override
	public void load(Path folderPath) throws IOException {
		this.folderPath = folderPath;
		loadAtlas();
		loadBlocks();
	}

	private void loadAtlas() throws IOException {
		ObjectMapper objectMapper = (new ObjectMapper()).configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		JsonNode itemTextureAtlasNode = objectMapper.readTree(folderPath.resolve(ITEM_TEXTURE_FILE).toFile());
		Map<String, JsonNode> itemTextureAtlas = new HashMap<>();
		itemTextureAtlasNode.path("texture_data").fields().forEachRemaining(entry -> itemTextureAtlas.put(entry.getKey(), entry.getValue()));

		objectMapper = (new ObjectMapper()).configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		JsonNode terrainTextureAtlasNode = objectMapper.readTree(folderPath.resolve(TERRAIN_TEXTURE_FILE).toFile());
		Map<String, JsonNode> terrainTextureAtlas = new HashMap<>();
		terrainTextureAtlasNode.path("texture_data").fields().forEachRemaining(entry -> terrainTextureAtlas.put(entry.getKey(), entry.getValue()));

		this.atlas = new Atlas(itemTextureAtlas, terrainTextureAtlas);
	}

	private void loadBlocks() throws IOException {
		this.blocks = Blocks.getParser(folderPath.resolve(BLOCKS_FILE).toFile(), this.atlas);
	}

	private static boolean shouldRemoveFile(Path filePath) {
		return !(
				filePath.endsWith("manifest.json")
						|| filePath.endsWith(BLOCKS_FILE)
						|| filePath.endsWith(ITEM_TEXTURE_FILE)
						|| filePath.endsWith(TERRAIN_TEXTURE_FILE)
		);
	}

	@Override
	public void cullUselessStoredFiles() throws IOException {
		Files.walk(folderPath)
				.filter(Files::isRegularFile)
				.sorted((o1, o2) -> -o1.compareTo(o2))
				.filter(ResourcePackParser::shouldRemoveFile)
				.forEach(path -> path.toFile().delete());
	}
}
