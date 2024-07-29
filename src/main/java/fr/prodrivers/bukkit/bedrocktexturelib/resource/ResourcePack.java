package fr.prodrivers.bukkit.bedrocktexturelib.resource;

import fr.prodrivers.bukkit.bedrocktexturelib.BedrockTextureLib;
import fr.prodrivers.bukkit.bedrocktexturelib.Log;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.parser.ResourcePackManifestParser;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.parser.ResourcePackParser;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.parser.blocks.Blocks;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.texture.Atlas;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.util.MinecraftVersion;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ResourcePack {
	private ResourcePackParser parser;

	public ResourcePack() {
		load();
	}

	private List<String> getVersions() {
		try {
			Class.forName("org.geysermc.geyser.api.GeyserApi");
			return GeyserApi.api().supportedBedrockVersions().stream().map(MinecraftVersion::versionString).toList();
		} catch(ClassNotFoundException e) {
			Log.warning("No local Geyser connector detected.");
		}

		return List.of(BedrockTextureLib.getInstance().getConfiguration().getFallbackMinecraftVersion());
	}

	private List<String> getPrimaryVersions(List<String> versions) {
		return versions.stream()
				.map(version -> {
					String[] versionTagNumbers = version.split("\\.");
					if(versionTagNumbers.length > 2) {
						versionTagNumbers[2] = "0";
						return String.join(".", versionTagNumbers);
					} else if(versionTagNumbers.length == 2) {
						return String.join(".", versionTagNumbers) + ".0";
					}
					return null;
				})
				.filter(Objects::nonNull)
				.toList();
	}

	private List<String> getShortVersions(List<String> versions) {
		return versions.stream()
				.map(version -> {
					String[] versionTagNumbers = version.split("\\.");
					if(versionTagNumbers.length > 2) {
						return versionTagNumbers[0] + "." + versionTagNumbers[1];
					}
					return String.join(".", versionTagNumbers);
				})
				.toList();
	}

	private void load() {
		List<String> versions = getVersions();
		if(!load(versions)) {
			if(!load(getPrimaryVersions(versions))) {
				if(!load(getShortVersions(versions))) {
					Log.severe("Used all version alternatives to get resource pack for version " + getVersions() + ", giving up.");
				}
			}
		}
	}

	private boolean load(List<String> versions) {
		return versions.stream().anyMatch(this::load);
	}

	private boolean load(@NonNull String version) {
		Log.info("Loading resource pack for Minecraft Bedrock version " + version);
		if(!Downloader.checkIsDownloaded(version)) {
			if(!Downloader.download(version)) {
				Log.severe("Could not download resource pack for Minecraft Bedrock version " + version);
				return false;
			}
		}

		File resourcePackPath = Downloader.getResourcePackPath(version);
		try {
			ResourcePackManifestParser manifestParser = new ResourcePackManifestParser(resourcePackPath);
			parser = manifestParser.getResourcePackParser();
			parser.load(resourcePackPath.toPath());
			parser.cullUselessStoredFiles();

			Log.info("Resource pack loaded.");
			return true;
		} catch(IOException e) {
			Log.severe("Could not parse resource pack at path : " + Downloader.getResourcePackPath(version), e);
		}
		return false;
	}

	public Atlas getAtlas() {
		return parser != null ? parser.getAtlas() : null;
	}

	public Blocks getBlocks() {
		return parser != null ? parser.getBlocks() : null;
	}
}
