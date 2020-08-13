package fr.prodrivers.bukkit.bedrockbridger.commands;

import fr.prodrivers.bukkit.bedrockbridger.BedrockBridge;
import org.bukkit.command.CommandSender;

public class ReloadCommand {
	public static boolean onCommand(CommandSender sender, String[] args) {
		if(sender.hasPermission("bedrockbridger.reload")) {
			BedrockBridge.getInstance().reload();
			BedrockBridge.getInstance().getChat().success(sender, "Configuration reloaded.");
			return true;
		}
		return false;
	}
}
