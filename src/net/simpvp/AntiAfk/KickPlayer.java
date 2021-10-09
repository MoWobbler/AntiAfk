package net.simpvp.AntiAfk;

import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class KickPlayer {
	
	private static long last_request = System.currentTimeMillis();

	public static void online_check() {
		
		if (System.currentTimeMillis() < (AntiAfk.online_check_seconds * 1000) + last_request) {
			return;
		}
		
		last_request = System.currentTimeMillis();

		TextComponent msg = new TextComponent("[Announcement] Please verify that you're online by moving ");
		msg.setColor(ChatColor.AQUA);
		
		for (Player p : GetAfkPlayers.afkPlayers) {
			p.spigot().sendMessage(msg);
			p.sendTitle(ChatColor.GOLD + "Afk Check", "Please verify that you're online", 10, 550, 20);
		}
		
		/* Activate in 20 seconds */
		final long activate_at = System.currentTimeMillis() + 20 * 1000;
		new BukkitRunnable() {
			@Override
			public void run() {
				if (System.currentTimeMillis() < activate_at) {
					return;
				}

				this.cancel();
				new BukkitRunnable() {
					@Override
					public void run() {
						Player p;
				 		for (Iterator<?> afkListIterator = AntiAfk.instance.getServer().getOnlinePlayers().iterator(); afkListIterator.hasNext();) {
				 			p = (Player) afkListIterator.next();
				 			if (GetAfkPlayers.isAfk(p)) {
				 				p.kickPlayer(ChatColor.RED + "Kicked for being afk in low tps");
				 			}
				 		}
					}
				}.runTaskLater(AntiAfk.instance, 0);
			}
		}.runTaskTimerAsynchronously(AntiAfk.instance, 20L, 20L);
	}
}
