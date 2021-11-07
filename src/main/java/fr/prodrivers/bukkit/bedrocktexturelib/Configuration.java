package fr.prodrivers.bukkit.bedrocktexturelib;

import org.bukkit.plugin.Plugin;

public class Configuration {
	private final Plugin plugin;

	public Configuration(Plugin plugin) {
		this.plugin = plugin;

		setup();
	}

	public void setup() {
		this.plugin.getConfig().addDefault("resourcePack.downloadUrl", "https://github.com/ZtechNetwork/MCBVanillaResourcePack/releases/download/v%VERSIONTAG%/VanillaResourcePack.zip");
	}

	public void reload() {
		this.plugin.reloadConfig();
	}

	public String getResourcePackUrl() {
		return this.plugin.getConfig().getString("resourcePack.downloadUrl");
	}
}
