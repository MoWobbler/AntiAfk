package net.simpvp.AntiAfk;

import org.bukkit.plugin.java.JavaPlugin;

public class AntiAfk extends JavaPlugin {
	
	public static JavaPlugin instance;

	@Override
	public void onEnable() {
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		instance = this;

		GetAfkPlayers.kick_players = AntiAfk.instance.getConfig().getStringList("Kick_players");
		GetAfkPlayers.minTps = AntiAfk.instance.getConfig().getDouble("Minimum_tps");
		KickPlayer.kickAttemptFrequency = AntiAfk.instance.getConfig().getInt("Kick_attempt");

		getCommand("antiafk").setExecutor(new AntiAfkCommand());
		GetAfkPlayers.checkForLowTps();
	}
}
