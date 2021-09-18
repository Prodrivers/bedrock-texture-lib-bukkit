package fr.prodrivers.bukkit.bedrocktexturelib;

import fr.prodrivers.bukkit.commons.Chat;
import fr.prodrivers.bukkit.commons.configuration.Configuration;
import fr.prodrivers.bukkit.commons.configuration.Messages;
import org.bukkit.plugin.Plugin;

public class EConfiguration extends Configuration {
	public String resourcePack_downloadUrl = "https://github.com/ZtechNetwork/MCBVanillaResourcePack/releases/download/v%VERSIONTAG%/VanillaResourcePack.zip";

	public EConfiguration(Plugin plugin, Messages messages, Chat chat) {
		super(plugin, chat, messages);
		init();
	}
}
