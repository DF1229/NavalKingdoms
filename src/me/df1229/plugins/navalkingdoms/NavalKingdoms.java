package me.df1229.plugins.navalkingdoms;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.df1229.plugins.navalkingdoms.cmds.*;

public class NavalKingdoms extends JavaPlugin implements Listener {
	
	/**
	 * Runs everytime the plugin is enabled, should be during server startup
	 */
	public void onEnable() {
		loadConfig();
		
		// All commands run through the hub command, removing the possiblity of ambiguity with other plugins
		this.getCommand("nk").setExecutor(new HubCommand());
	}
	
	/**
	 * Loads config.yml
	 * Defaults to the config file included during plugin compilation, if no existing config is found during startup
	 */
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
}
