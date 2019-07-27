package me.df1229.plugins.navalkingdoms.cmds;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.df1229.plugins.navalkingdoms.NavalKingdoms;

public class ClaimRegion {
	
	private static Plugin plugin = NavalKingdoms.getPlugin(NavalKingdoms.class);

	public boolean executeCmd(CommandSender sender, Command command, String label, String[] args) {
		
		String prefix = plugin.getConfig().getString("chat-prefix");
		
		if (prefix == null) {
			sender.sendMessage(ChatColor.RED + "Internal Plugin Error: Cannot load plugin's chat prefix, is it in config.yml?");
		}
		
		WorldEditPlugin wePlugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (!(wePlugin instanceof WorldEditPlugin)) {
			sender.sendMessage(ChatColor.RED + prefix + " Error: Unable to load WorldEdit");
		}
		
		// Below is the biggest factor in compatibility fix for worldguard/-edit 6.x
		WorldGuardPlugin wgPlugin = WGBukkit.getPlugin();
		if (!(wgPlugin instanceof WorldGuardPlugin)) {
			sender.sendMessage(ChatColor.RED + prefix + " Error: Unable to load WorldGuard");
			return true;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + prefix + " You can only register a claim as a player");
			return true;
		}
		
		Player player = (Player) sender; // Player object of sender
		
		RegionContainer container = wgPlugin.getRegionContainer(); // RegionContainer object of all regions
		
		if (container == null) {
			player.sendMessage(ChatColor.RED + prefix + " Error: Could not load WorldGuard data, please contact the server's staff!");
			return true;
		}
		
		RegionManager regions = container.get(player.getWorld()); // RegionManager object for all regions in the player's world.
		
		if (regions == null) {
			player.sendMessage(ChatColor.RED + prefix + " Error: Could not load " + player.getWorld().getName() + "'s regions, please contact the server's staff!");
			return true;
		}
		
		String playerName = player.getName(); // Player's name
		UUID playerUUID = player.getUniqueId(); // Player's UUID as a string
		ProtectedRegion playerRegion = regions.getRegion(playerName); // Attempt to fetch any region with the player's name
		
		if (playerRegion == null) {			
			Location playerLoc = player.getLocation(); // Location object for the player's location
			
			Double playerX = playerLoc.getX(); // Original X
			Double playerY = playerLoc.getY(); // Original Y
			Double playerZ = playerLoc.getZ(); // Original Z
			
			BlockVector pos1 = new BlockVector(playerX - 50, playerY - 200, playerZ - 50); // Modified coordinates for pos1
			BlockVector pos2 = new BlockVector(playerX + 50, playerY + 200, playerZ + 50); // Modified coordinates for pos2
			
			ProtectedRegion newRegion = new ProtectedCuboidRegion(playerName, pos1, pos2); // Definition of the new region 
			
			// TODO: Add overlap checking
			
			DefaultDomain owners = newRegion.getOwners(); // List of current owners of the region
			owners.addPlayer(playerUUID);
			
			newRegion.setFlag(DefaultFlag.BUILD, StateFlag.State.DENY); // Build = deny
			newRegion.setFlag(DefaultFlag.BLOCK_BREAK, StateFlag.State.DENY); // Block_break = deny
			newRegion.setFlag(DefaultFlag.BLOCK_PLACE, StateFlag.State.DENY); // Block_place = deny
			newRegion.setFlag(DefaultFlag.GREET_MESSAGE, ChatColor.AQUA + "Now entering " + playerName + "'s region."); // Greet message
			newRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, ChatColor.GRAY + "Now leaving " + playerName + "'s region."); // Farewell message
			
			regions.addRegion(newRegion); // New region gets added to the regions of that world
			
			// TODO: Add feature support for territorial mark above the region
			
			player.sendMessage(ChatColor.GREEN + prefix + " Your region has been created!");
				
			return true;
		} else {
			// Region already exists
			player.sendMessage(ChatColor.AQUA + prefix + " You already have a claim, use " + ChatColor.ITALIC + "/nk unclaim " + ChatColor.RESET + ChatColor.AQUA + "to remove your region.");
			return true;
		}
		
	}

}
