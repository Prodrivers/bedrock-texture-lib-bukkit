package fr.prodrivers.bukkit.bedrockbridger.material;

import com.cryptomorin.xseries.XMaterial;
import fr.prodrivers.bukkit.bedrockbridger.resource.ResourcePack;
import org.bukkit.Material;

public class TextureMapper {
	private ResourcePack resourcePack;

	public TextureMapper(ResourcePack resourcePack) {
		this.resourcePack = resourcePack;
	}

	public String getTexturePath(Material material) {
		XMaterial compatMaterial = XMaterial.matchXMaterial(material);
		if(compatMaterial.getLegacy().length == 0) {
			if(resourcePack.getBlocks() != null) {
				return resourcePack.getBlocks().getBestTexture(compatMaterial.name().toLowerCase(), compatMaterial.getData());
			} else {
				return null;
			}
		}

		for(String compatName : compatMaterial.getLegacy()) {
			if(!compatName.contains(".")) {
				String texture = resourcePack.getBlocks().getBestTexture(compatName.toLowerCase(), compatMaterial.getData());
				if(texture != null) {
					return texture;
				}
			}
		}

		return null;
	}
}
