package net.simpvp.AntiAfk;

import java.util.List;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public class AntiAfk extends JavaPlugin {
	
	public static JavaPlugin instance;

	static Double minTps;
	static Integer kickAttemptFrequency;
	static List<?> kick_players;


	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		instance = this;
		minTps = AntiAfk.instance.getConfig().getDouble("Minimum_tps");
		kickAttemptFrequency = AntiAfk.instance.getConfig().getInt("Kick_attempt");
		kick_players = AntiAfk.instance.getConfig().getStringList("Kick_players").stream().map(UUID::fromString).collect(java.util.stream.Collectors.toList());
		GetAfkPlayers.checkForLowTps();
	}
}
