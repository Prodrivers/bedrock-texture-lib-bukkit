package fr.prodrivers.bukkit.bedrockbridger.resource.texture;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;

public class BaseTexture {
	protected final JsonNode root;

	public BaseTexture(JsonNode node) {
		this.root = node;
	}

	public String getTexturePath(byte blockData) {
		if(root == null) {
			return null;
		}

		JsonNode node = root.path("textures");

		if(node.isMissingNode()) {
			return null;
		} else if(node.isTextual()) {
			return node.asText();
		} else if(node.isArray()) {
			int index = 0;
			for(Iterator<JsonNode> it = node.elements(); it.hasNext(); ) {
				JsonNode child = it.next();

				if(child.isTextual() && index == blockData) {
					return child.asText();
				}

				index++;
			}
		}

		return null;
	}
}
