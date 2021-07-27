package fr.prodrivers.bukkit.bedrockbridger;

import fr.prodrivers.bukkit.bedrockbridger.commands.MainCommandExecutor;
import fr.prodrivers.bukkit.bedrockbridger.event.BedrockEventManager;
import fr.prodrivers.bukkit.bedrockbridger.material.TextureMapper;
import fr.prodrivers.bukkit.bedrockbridger.resource.ResourcePack;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class BedrockBridge extends JavaPlugin implements org.bukkit.event.Listener {
	private static BedrockBridge instance;

	private EConfiguration configuration;
	private EChat chat;

	private BedrockEventManager eventManager;
	private ResourcePack resourcePack;
	private TextureMapper textureMapper;

	public static BedrockBridge getInstance() {
		return instance;
	}

	public EConfiguration getConfiguration() {
		return configuration;
	}

	public EChat getChat() {
		return chat;
	}

	public BedrockEventManager getEventManager() {
		return eventManager;
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

		chat = new EChat(pluginDescription.getName());
		configuration = new EConfiguration(this, null, chat);

		getServer().getPluginManager().registerEvents(this, this);

		eventManager = new BedrockEventManager();

		getCommand(MainCommandExecutor.LABEL).setExecutor(new MainCommandExecutor());

		loadResources(true);

		Log.info(pluginDescription.getName() + " has been enabled!");
	}

	@Override
	public void onDisable() {
		PluginDescriptionFile pluginDescription = this.getDescription();

		configuration.save();
		Log.info(" Saved configuration.");

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
