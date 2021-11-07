package fr.prodrivers.bukkit.bedrocktexturelib;

import fr.prodrivers.bukkit.bedrocktexturelib.commands.MainCommandExecutor;
import fr.prodrivers.bukkit.bedrocktexturelib.material.TextureMapper;
import fr.prodrivers.bukkit.bedrocktexturelib.resource.ResourcePack;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class BedrockTextureLib extends JavaPlugin implements org.bukkit.event.Listener {
	private static BedrockTextureLib instance;

	private Configuration configuration;

	private ResourcePack resourcePack;
	private TextureMapper textureMapper;

	public static BedrockTextureLib getInstance() {
		return instance;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public ResourcePack getResourcePack() {
		return resourcePack;
	}

	public TextureMapper getTextureMapper() {
		return textureMapper;
	}

	@Override
	public void onEnable() {
		PluginDescriptionFile pluginDescription = this.getDescription();

		if(instance != null) {
			Log.warning("Plugin is already initialized, overriding existing instance.");
		}

		instance = this;

		Log.init();

		configuration = new Configuration(this);

		getServer().getPluginManager().registerEvents(this, this);

		getCommand(MainCommandExecutor.LABEL).setExecutor(new MainCommandExecutor());

		loadResources(true);

		Log.info(pluginDescription.getName() + " has been enabled!");
	}

	@Override
	public void onDisable() {
		PluginDescriptionFile pluginDescription = this.getDescription();

		Log.info(pluginDescription.getName() + " has been disabled!");
	}

	private void loadResources(boolean delay) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
			resourcePack = new ResourcePack();
			textureMapper = new TextureMapper(resourcePack);
		}, delay ? 2 * 20L : 0);
	}

	public void reload() {
		configuration.reload();
		loadResources(false);
	}
}
