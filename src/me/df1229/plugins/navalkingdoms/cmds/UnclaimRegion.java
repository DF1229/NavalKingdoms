package me.df1229.plugins.navalkingdoms.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.df1229.plugins.navalkingdoms.NavalKingdoms;

public class UnclaimRegion {
	
	private static Plugin plugin = NavalKingdoms.getPlugin(NavalKingdoms.class);

	public void executeCmd(CommandSender sender, Command command, String label, String[] args) {
		
		String prefix = plugin.getConfig().getString("chat-prefix");
		
		if (prefix == null) {
			sender.sendMessage(ChatColor.RED + "Internal Plugin Error: Cannot load plugin's chat prefix, is it in config.yml?");
		}
		
		// Below is the biggest factor in compatibility fix for worldguard/-edit 6.x
		WorldGuardPlugin wgPlugin = WGBukkit.getPlugin();
		if (!(wgPlugin instanceof WorldGuardPlugin)) {
			sender.sendMessage(ChatColor.RED + prefix + " Error: WorldGuard could not be loaded.");
			return;
		}
		
		// Check if sender is a player, if not: send an error and exit early
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + prefix + " Error: You can only use this command as a player.");
			return;
		}
		
		Player player = (Player) sender; // Bukkit's Player object of the sender
		
		RegionContainer container = wgPlugin.getRegionContainer(); // All regions wrapped up per world in a RegionContainer
		
		// Check if the container was loaded successfully, if not: send an error and exit early
		if (container == null) {
			player.sendMessage(ChatColor.RED + prefix + " Error 502: Could not load claim data, please contact the server's staff.");
			return;
		}
		
		RegionManager regions = container.get(player.getWorld()); // RegionManager for the world the player is in
				
		// Check if the regions were loaded successfully, if not: send an error and exit early
		if (regions == null) {
			player.sendMessage(ChatColor.RED + prefix + " Could not load " + player.getWorld().getName() + "'s claims, please try again and contact the server's staff!");
			return;
		}
		
		String playerName = player.getName(); // Player's name stored as a String, used as region ID
		ProtectedRegion playerRegion = regions.getRegion(playerName); // The region that is associated with the player's name (region ID)
		
		// Check to see if the region exists.
		// If not: send an error and exit early
		// if yes: remove the region and send the player a message.
		if (playerRegion == null) {
			player.sendMessage(ChatColor.RED + prefix + " You don't have a claim, make one with " + ChatColor.ITALIC + "/nk claim");
			return;
		} else {
			
			// TODO: Add confirmation before removing regions
			
			regions.removeRegion(playerName, RemovalStrategy.UNSET_PARENT_IN_CHILDREN);
			player.sendMessage(ChatColor.GREEN + prefix + " Your region was removed, any children of the region still exist. If you need these removed, contact staff.");
			return;
		}
		
	}

}
