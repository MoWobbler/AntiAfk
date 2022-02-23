package net.simpvp.AntiAfk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class KickPlayer {

	private static long last_request = System.currentTimeMillis();

	/* If the player doesn't move within 20 seconds, kick them */
	public static void online_check(AfkPlayer afkPlayer) {
		
		if (System.currentTimeMillis() < (25000) + last_request) {
			return;
		}

		last_request = System.currentTimeMillis();
		Player player = afkPlayer.getPlayer();
		afkPlayer.setPossibleKickPending(true);
		player.sendMessage(ChatColor.AQUA + "[Announcement] Please verify that you're online by moving or looking around");
		player.sendTitle(ChatColor.GOLD + "Afk Check", "Please verify that you're online", 10, 550, 20);
		
		/* Activate in 20 seconds */
		Bukkit.getScheduler().scheduleSyncDelayedTask(AntiAfk.instance, () -> {
				if (afkPlayer != null && afkPlayer.getIsAfk()) {
					AntiAfk.instance.getLogger().info(afkPlayer.getPlayer().getDisplayName() + " was kicked for being afk");
					player.kickPlayer(ChatColor.RED + "Kicked for being afk in low tps");
				}
		}, 400L);
	}
}
