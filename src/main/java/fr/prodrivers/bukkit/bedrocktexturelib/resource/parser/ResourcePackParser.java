package fr.prodrivers.bukkit.bedrocktexturelib.resource.parser;

import fr.prodrivers.bukkit.bedrocktexturelib.resource.parser.blocks.Blocks;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.texture.Atlas;

import java.io.IOException;
import java.nio.file.Path;

public interface ResourcePackParser {
	public void load(Path folderPath) throws IOException;

	public Atlas getAtlas();

	public Blocks getBlocks();

	public void cullUselessStoredFiles() throws IOException;
}
