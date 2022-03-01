package net.simpvp.AntiAfk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;


public class KickPlayer {

	private static long last_request = System.currentTimeMillis();
	private static final ArrayList<Player> exemptPlayers = new ArrayList<>();
	public static int task;


	/* If the player doesn't move within 20 seconds, kick them */
	public static void online_check() {
		
		if (System.currentTimeMillis() < (AntiAfk.kickAttemptFrequency * 1000) + last_request) {
			return;
		}

		last_request = System.currentTimeMillis();
		exemptPlayers.clear();

		/* Tell afk players to move */
		for (UUID uuid: GetAfkPlayers.playerLocations.keySet()) {
			Player player = Bukkit.getPlayer(uuid);
			if (player == null) continue;
			if (!(GetAfkPlayers.isPlayerAfk(GetAfkPlayers.playerLocations.get(player.getUniqueId()), player))) {
				exemptPlayers.add(player);
				continue;
			}
			player.sendMessage(ChatColor.AQUA + "[Announcement] Please verify that you're online by moving and looking around");
			player.sendTitle(ChatColor.GOLD + "Afk Check", "Please verify that you're online", 10, 550, 20);
		}

		/* Activate in 20 seconds */
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(AntiAfk.instance, new Runnable() {

			@Override
			public void run() {
				for (UUID uuid: GetAfkPlayers.playerLocations.keySet()) {
					Player player = Bukkit.getPlayer(uuid);

					if (player == null) continue;


					if (!(GetAfkPlayers.isPlayerAfk(GetAfkPlayers.playerLocations.get(player.getUniqueId()), player)) && !exemptPlayers.contains(player)) {
						GetAfkPlayers.playerLocations.replace(player.getUniqueId(), player.getLocation());
						player.resetTitle();
						player.sendMessage(ChatColor.GREEN + "You're no longer afk");
						exemptPlayers.add(player);
						continue;
					}

					if (System.currentTimeMillis() > last_request + 20000) {
						if (!exemptPlayers.contains(player)) {
							player.kickPlayer(ChatColor.RED + "Kicked for being afk in low tps");
							AntiAfk.instance.getLogger().info(player.getDisplayName() + " was kicked for being afk");
						}
						Bukkit.getScheduler().cancelTask(task);
					}
				}
			}
		}, 20, 20);
	}
}
