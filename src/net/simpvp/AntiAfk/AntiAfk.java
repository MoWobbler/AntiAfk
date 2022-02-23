package net.simpvp.AntiAfk;

import java.util.List;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public class AntiAfk extends JavaPlugin {
	
	public static JavaPlugin instance;

	static Double minTps;
	static Integer afkSecs;
	static Integer activityCheckFrequency;
	static List<?> kick_players;


	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		instance = this;
		minTps = AntiAfk.instance.getConfig().getDouble("Minimum_tps");
		afkSecs = AntiAfk.instance.getConfig().getInt("Afk_seconds");
		activityCheckFrequency = AntiAfk.instance.getConfig().getInt("Activity_Check_Frequency");
		kick_players = AntiAfk.instance.getConfig().getStringList("Kick_players").stream().map(UUID::fromString).collect(java.util.stream.Collectors.toList());
		GetAfkPlayers.checkForAfkPlayers();
		getServer().getPluginManager().registerEvents(new EventListener(), this);
	}
}
