package net.simpvp.AntiAfk;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class KickPlayer {
	
	private static long last_request = System.currentTimeMillis();

	public static void online_check() {
		
		if (System.currentTimeMillis() < (1 * 60 * 1000) + last_request) {
			return;
		}

		for (Player p : GetAfkPlayers.afkPlayers) {
			p.sendMessage(ChatColor.RED + "You are about to be kicked for being afk. Please move.");
		}
		// Run this in 20 seconds
		new BukkitRunnable() {
		     @Override
		     public void run() {
		 		for (Player p : GetAfkPlayers.afkPlayers) {
					p.kickPlayer(ChatColor.RED + "Kicked for being afk in low tps.");
				}
		 		last_request = System.currentTimeMillis();
		          cancel();
		     }
		}.runTaskTimer(AntiAfk.instance, 400, 1);
	}
}
