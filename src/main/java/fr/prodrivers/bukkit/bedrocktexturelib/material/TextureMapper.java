package fr.prodrivers.bukkit.bedrocktexturelib.material;

import com.cryptomorin.xseries.XMaterial;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.ResourcePack;
import org.bukkit.Material;

public class TextureMapper {
	private ResourcePack resourcePack;

	public TextureMapper(ResourcePack resourcePack) {
		this.resourcePack = resourcePack;
	}

	public String getTexturePath(Material material) {
		if(resourcePack.getBlocks() == null) {
			return null;
		}

		XMaterial compatMaterial = XMaterial.matchXMaterial(material);
		if(compatMaterial.getLegacy().length == 0) {
			return resourcePack.getBlocks().getBestTexture(compatMaterial.name().toLowerCase(), compatMaterial.getData());
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
