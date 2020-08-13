package fr.prodrivers.bukkit.bedrockbridger;

import fr.prodrivers.bukkit.commons.configuration.Configuration;
import fr.prodrivers.bukkit.commons.configuration.Messages;
import org.bukkit.plugin.Plugin;

public class EConfiguration extends Configuration {
	public String resourcePack_downloadUrl = "https://github.com/ZtechNetwork/MCBVanillaResourcePack/releases/download/v%VERSIONTAG%/VanillaResourcePack.zip";

	public EConfiguration(Plugin plugin, EChat chat) {
		super(plugin, Messages.class, chat);
	}
}
