package me.df1229.plugins.navalkingdoms;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.df1229.plugins.navalkingdoms.cmds.*;

public class NavalKingdoms extends JavaPlugin implements Listener {
	
	public void onEnable() {
		
		this.getCommand("nkclaim").setExecutor(new ClaimRegion());
		this.getCommand("nkunclaim").setExecutor(new UnclaimRegion());
		
	}
	
}
