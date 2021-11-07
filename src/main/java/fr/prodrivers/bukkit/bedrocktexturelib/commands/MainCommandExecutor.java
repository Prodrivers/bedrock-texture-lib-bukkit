package fr.prodrivers.bukkit.bedrocktexturelib.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommandExecutor implements CommandExecutor {
	public static final String LABEL = "bedrocktexturelib";

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase(LABEL)) {
			if(args.length > 0) {
				switch(args[0]) {
					case "reload":
						return ReloadCommand.onCommand(sender, args);
				}
			} else {
				if(sender.hasPermission("bedrocktexturelib.help")) {
					sender.sendMessage("[BedrockTextureLib] " + ChatColor.RED + "Unknown command.");
					return true;
				}
			}
		}
		return false;
	}
}
