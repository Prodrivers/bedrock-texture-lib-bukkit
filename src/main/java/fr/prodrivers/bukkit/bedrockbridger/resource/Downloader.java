package fr.prodrivers.bukkit.bedrockbridger.resource;

import fr.prodrivers.bukkit.bedrockbridger.BedrockBridge;
import fr.prodrivers.bukkit.bedrockbridger.Constants;
import fr.prodrivers.bukkit.bedrockbridger.Log;
import fr.prodrivers.bukkit.bedrockbridger.protocol.MinecraftVersion;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Downloader {
	private static Path getStoragePath() {
		return BedrockBridge.getInstance().getDataFolder().toPath().resolve(Constants.RESOURCEPACK_STORAGE_FOLDER);
	}

	public static File getResourcePackPath(String version) {
		return getStoragePath().resolve(version).toFile();
	}

	public static boolean checkIsDownloaded(String version) {
		return getStoragePath().resolve(version).toFile().isDirectory();
	}

	private static String getDownloadUrl(String version) {
		return BedrockBridge.getInstance().getConfiguration().resourcePack_downloadUrl.replaceAll("%VERSIONTAG%", version);
	}

	public static boolean download(String version) {
		String downloadUrlStr = getDownloadUrl(version);
		URL downloadUrl;
		try {
			downloadUrl = new URL(downloadUrlStr);
		} catch(MalformedURLException e) {
			Log.severe("Invalid resource pack download URL: " + downloadUrlStr);
			return false;
		}

		File destination = getResourcePackPath(version);
		if(!destination.mkdirs()) {
			Log.severe("Could not create " + version + " resource pack directory.");
			return false;
		}

		Log.info("Downloading resource pack at URL " + downloadUrlStr + "...");

		try(BufferedInputStream in = new BufferedInputStream(downloadUrl.openStream())) {
			try(ZipInputStream zis = new ZipInputStream(in)) {
				byte[] buffer = new byte[1024];
				ZipEntry zipEntry = zis.getNextEntry();
				while(zipEntry != null) {
					File fileToExtract = newFile(destination, zipEntry);
					if(!zipEntry.isDirectory()) {
						try(FileOutputStream fos = new FileOutputStream(fileToExtract)) {
							int len;
							while((len = zis.read(buffer)) > 0) {
								fos.write(buffer, 0, len);
							}
						} catch(IOException e) {
							throw new IOException("Could not write file from ZIP: " + fileToExtract, e);
						}
					} else {
						if(!fileToExtract.mkdir()) {
							throw new IOException("Could not create directory: " + fileToExtract);
						}
					}
					zis.closeEntry();
					zipEntry = zis.getNextEntry();
				}
				zis.closeEntry();
			} catch(IOException e) {
				throw new IOException("Could not get ZIP input stream for URL: " + downloadUrl, e);
			}
		} catch(IOException e) {
			Log.severe("Could not get buffered input stream for URL: " + downloadUrl, e);
			try {
				Files.walk(destination.toPath())
						.map(Path::toFile)
						.sorted((o1, o2) -> -o1.compareTo(o2))
						.forEach(File::delete);
			} catch(IOException e1) {
				Log.severe("Could not remove resource pack directory in response to an extraction error: ", e1);
			}
			return false;
		}

		Log.info("Downloading of resource pack finished.");
		return true;
	}

	private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if(!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}
}
