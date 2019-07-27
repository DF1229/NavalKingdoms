package me.df1229.plugins.navalkingdoms.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor {
	
	private static ClaimRegion claimExec = new ClaimRegion();
	private static UnclaimRegion unclaimExec = new UnclaimRegion();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length != 1) {
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "[NavalKingdoms] You can't use that command here!");
			return true;
		}
		
		Player player = (Player) sender;
		
		
		switch (args[0]) {
		case "claim":
			claimExec.executeCmd(sender, cmd, label, args);
			break;
		case "unclaim":
			unclaimExec.executeCmd(sender, cmd, label, args);
			break;
		default:
			player.sendMessage(ChatColor.RED + "[NavalKingdoms] Command error: could not parse command, please notify staff");
			break;
		}
		
		return true;
	}

}
