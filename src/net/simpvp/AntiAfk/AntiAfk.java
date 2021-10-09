package net.simpvp.AntiAfk;

import java.util.List;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public class AntiAfk extends JavaPlugin {
	
	public static JavaPlugin instance;
	
	static GetTps gettps = new GetTps();
	static Double min_tps;
	static Integer afk_secs;
	static Integer online_check_seconds;
	static Integer scheduler_seconds;
	static List<?> kick_players;
	static Boolean afk_message;
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		instance = this;
		min_tps = AntiAfk.instance.getConfig().getDouble("Minimum_tps");	
		afk_secs = AntiAfk.instance.getConfig().getInt("Afk_seconds");
		online_check_seconds = AntiAfk.instance.getConfig().getInt("Online_check_seconds");
		scheduler_seconds = AntiAfk.instance.getConfig().getInt("Scheduler_check_seconds");
		kick_players = AntiAfk.instance.getConfig().getStringList("Kick_players").stream().map(UUID::fromString).collect(java.util.stream.Collectors.toList());
		afk_message = AntiAfk.instance.getConfig().getBoolean("Afk_message");
		gettps.variables();	
		GetAfkPlayers.setPlayersAfk();
		getServer().getPluginManager().registerEvents(new EventListener(), this);
		OnlineCheck oc_instance = new OnlineCheck();
		getCommand("checkonline").setExecutor(oc_instance);
		getCommand("on").setExecutor(oc_instance);
	}
}
