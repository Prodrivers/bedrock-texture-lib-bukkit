package fr.prodrivers.bukkit.bedrocktexturelib.commands;

import fr.prodrivers.bukkit.bedrocktexturelib.BedrockTextureLib;
import org.bukkit.command.CommandSender;

public class ReloadCommand {
	public static boolean onCommand(CommandSender sender, String[] args) {
		if(sender.hasPermission("bedrockbridger.reload")) {
			BedrockTextureLib.getInstance().reload();
			BedrockTextureLib.getInstance().getChat().success(sender, "Configuration reloaded.");
			return true;
		}
		return false;
	}
}
