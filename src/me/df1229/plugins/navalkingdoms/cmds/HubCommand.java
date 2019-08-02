package me.df1229.plugins.navalkingdoms.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import me.df1229.plugins.navalkingdoms.NavalKingdoms;

public class HubCommand implements CommandExecutor {
	
	private static ClaimRegion claimExec = new ClaimRegion();
	private static UnclaimRegion unclaimExec = new UnclaimRegion();
	
	private Plugin plugin = NavalKingdoms.getPlugin(NavalKingdoms.class);
	private String prefix = plugin.getConfig().getString("chat-prefix");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (prefix == null) {
			sender.sendMessage(ChatColor.RED + "Internal Plugin Error: Cannot load plugin's chat prefix, is it in config.yml?");
		}
		
		if (args.length != 1) {
			return false;
		}
		
		switch (args[0]) {
		case "claim":
			claimExec.executeCmd(sender, cmd, label, args);
			break;
		case "unclaim":
			unclaimExec.executeCmd(sender, cmd, label, args);
			break;
		case "reload":
			reloadConfig(sender);
			break;			
		default:
			sender.sendMessage(ChatColor.RED + prefix + " Command error: could not parse command, please notify staff");
			break;
		}
		
		return true;
	}
	
	private void reloadConfig(CommandSender sender) {
		
		if (!sender.hasPermission("navalkingdoms.reload")) {
			sender.sendMessage(ChatColor.RED + prefix + " You don't have permission to do that");
			return;
		}
		
		plugin.reloadConfig();
		reloadPrefix();
		sender.sendMessage(ChatColor.GREEN + prefix + " Config.yml reloaded");
	}
	
	public void reloadPrefix() {
		prefix = plugin.getConfig().getString("chat-prefix");
	}

}
