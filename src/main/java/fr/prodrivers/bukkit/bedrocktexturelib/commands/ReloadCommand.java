package fr.prodrivers.bukkit.bedrocktexturelib.commands;

import fr.prodrivers.bukkit.bedrocktexturelib.BedrockTextureLib;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadCommand {
	public static boolean onCommand(CommandSender sender, String[] args) {
		if(sender.hasPermission("bedrocktexturelib.reload")) {
			BedrockTextureLib.getInstance().reload();
			sender.sendMessage("[BedrockTextureLib] " + ChatColor.GREEN + "Configuration reloaded.");
			return true;
		}
		return false;
	}
}
