package me.df1229.plugins.navalkingdoms.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class UnclaimRegion implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		// Below is the biggest factor in compatibility fix for worldguard/-edit 6.x
		WorldGuardPlugin wgPlugin = WGBukkit.getPlugin();
		if (!(wgPlugin instanceof WorldGuardPlugin)) {
			sender.sendMessage(ChatColor.RED + "[NavalKingdoms] Error: WorldGuard could not be loaded.");
			return true;
		}
		
		// Check if sender is a player, if not: send an error and exit early
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "[NavalKingdoms] Error: You can only use this command as a player.");
			return true;
		}
		
		Player player = (Player) sender; // Bukkit's Player object of the sender
		//LocalPlayer lPlayer = WorldGuardPlugin.inst().wrapPlayer(player); // WorldGuard's LocalPlayer object of the player
		
		RegionContainer container = wgPlugin.getRegionContainer(); // All regions wrapped up per world in a RegionContainer
		
		// Check if the container was loaded successfully, if not: send an error and exit early
		if (container == null) {
			player.sendMessage(ChatColor.RED + "[NavalKingdoms] Error 502: Could not load claim data, please contact the server's staff.");
			return true;
		}
		
		RegionManager regions = container.get(player.getWorld()); // RegionManager for the world the player is in
				
		// Check if the regions were loaded successfully, if not: send an error and exit early
		if (regions == null) {
			player.sendMessage(ChatColor.RED + "[NavalKingdoms] Could not load " + player.getWorld().getName() + "'s claims, please try again and contact the server's staff!");
			return true;
		}
		
		String playerName = player.getName(); // Player's name stored as a String, used as region ID
		ProtectedRegion playerRegion = regions.getRegion(playerName); // The region that is associated with the player's name (region ID)
		
		// Check to see if the region exists.
		// If not: send an error and exit early
		// if yes: remove the region and send the player a message.
		if (playerRegion == null) {
			player.sendMessage(ChatColor.RED + "[NavalKindoms] You don't have a region, make one with " + ChatColor.ITALIC + "/claim");
			return true;
		} else {
			
			// TODO: Add confirmation before removing regions
			
			regions.removeRegion(playerName, RemovalStrategy.UNSET_PARENT_IN_CHILDREN);
			player.sendMessage(ChatColor.GREEN + "[NavalKingdoms] Your region was removed, any children of the region still exist. If you need these removed, contact staff.");
			return true;
		}
		
	}

}
