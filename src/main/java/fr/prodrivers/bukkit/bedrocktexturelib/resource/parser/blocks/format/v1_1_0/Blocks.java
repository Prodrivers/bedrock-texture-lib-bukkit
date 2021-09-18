package fr.prodrivers.bukkit.bedrocktexturelib.resource.parser.blocks.format.v1_1_0;

import com.fasterxml.jackson.databind.JsonNode;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.texture.Atlas;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Blocks extends fr.prodrivers.bukkit.bedrocktexturelib.resource.parser.blocks.Blocks {
	public Blocks(File filePath, Atlas atlas) throws IOException {
		super(filePath, atlas);
	}

	@Override
	public Collection<Map.Entry<String, String>> getAllTextureNames(String name) {
		JsonNode node = root.path(name).path("textures");

		if(node.isMissingNode()) {
			return Collections.emptyList();
		}

		if(node.isTextual()) {
			return Collections.singletonList(new AbstractMap.SimpleImmutableEntry<>(null, node.asText()));
		}

		List<Map.Entry<String, String>> textures = new ArrayList<>();
		for(Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
			Map.Entry<String, JsonNode> element = it.next();

			if(element.getValue().isTextual()) {
				textures.add(new AbstractMap.SimpleImmutableEntry<>(element.getKey(), element.getValue().asText()));
			}
		}

		return textures.size() != 0 ? textures : Collections.emptyList();
	}

	@Override
	public String getBestTextureName(String name) {
		Collection<Map.Entry<String, String>> textures = getAllTextureNames(name);
		if(textures.size() == 0) {
			return null;
		} else if(textures.size() == 1) {
			return textures.iterator().next().getValue();
		}

		for(Map.Entry<String, String> texture : textures) {
			if(texture.getKey().equalsIgnoreCase("front")) {
				return texture.getValue();
			}
		}

		for(Map.Entry<String, String> texture : textures) {
			if(texture.getKey().equalsIgnoreCase("south")) {
				return texture.getValue();
			}
		}

		for(Map.Entry<String, String> texture : textures) {
			if(texture.getKey().equalsIgnoreCase("side")) {
				return texture.getValue();
			}
		}

		return textures.iterator().next().getValue();
	}
}
