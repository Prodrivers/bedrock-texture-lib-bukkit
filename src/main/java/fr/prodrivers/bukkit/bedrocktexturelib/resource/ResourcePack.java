package fr.prodrivers.bukkit.bedrocktexturelib.resource;

import fr.prodrivers.bukkit.bedrocktexturelib.BedrockTextureLib;
import fr.prodrivers.bukkit.bedrocktexturelib.Log;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.parser.ResourcePackManifestParser;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.parser.ResourcePackParser;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.parser.blocks.Blocks;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.texture.Atlas;
import org.geysermc.connector.network.BedrockProtocol;

import java.io.File;
import java.io.IOException;

public class ResourcePack {
	private ResourcePackParser parser;

	public ResourcePack() {
		load();
	}

	private String getVersion() {
		try {
			Class.forName("org.geysermc.connector.network.BedrockProtocol");
			return BedrockProtocol.DEFAULT_BEDROCK_CODEC.getMinecraftVersion();
		} catch(ClassNotFoundException e) {
			Log.warning("No local Geyser connector detected.");
		}

		return BedrockTextureLib.getInstance().getConfiguration().getFallbackMinecraftVersion();
	}

	private String getPrimaryVersion() {
		String versionTag = getVersion();
		if(versionTag != null) {
			String[] versionTagNumbers = versionTag.split("\\.");
			if(versionTagNumbers.length > 2) {
				versionTagNumbers[2] = "0";
				return String.join(".", versionTagNumbers);
			} else if(versionTagNumbers.length == 2) {
				return String.join(".", versionTagNumbers) + ".0";
			}
		}
		return null;
	}

	private String getVersionShort() {
		String versionTag = getVersion();
		if(versionTag != null) {
			String[] versionTagNumbers = versionTag.split("\\.");
			if(versionTagNumbers.length > 2) {
				return versionTagNumbers[0] + "." + versionTagNumbers[1];
			}
			return String.join(".", versionTagNumbers);
		}
		return null;
	}

	private void load() {
		if(!load(getVersion())) {
			if(!load(getPrimaryVersion())) {
				if(!load(getVersionShort())) {
					Log.severe("Used all version alternatives to get resource pack for version " + getVersion() + ", giving up.");
				}
			}
		}
	}

	private boolean load(String version) {
		if(version != null) {
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
		} else {
			Log.severe("Invalid version provided for resource pack loading, reported version by proxy is " + getVersion());
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
