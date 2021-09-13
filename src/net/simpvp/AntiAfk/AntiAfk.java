package net.simpvp.AntiAfk;

import org.bukkit.plugin.java.JavaPlugin;


public class AntiAfk extends JavaPlugin {
	
	public static JavaPlugin instance;
	
	static GetTps gettps = new GetTps();
	static Double min_tps;
	static Integer afk_secs;
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		instance = this;
		min_tps = AntiAfk.instance.getConfig().getDouble("Minimum_tps");	
		afk_secs = AntiAfk.instance.getConfig().getInt("Afk_seconds");
		gettps.variables();	
		GetAfkPlayers.setPlayersAfk();
		getServer().getPluginManager().registerEvents(new EventListener(), this);
	}
}
