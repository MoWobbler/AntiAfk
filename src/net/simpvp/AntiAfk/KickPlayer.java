package net.simpvp.AntiAfk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;


public class KickPlayer {

	static Integer kickAttemptFrequency;
	private static long last_request = System.currentTimeMillis();
	static final ArrayList<Player> exemptPlayers = new ArrayList<>();
	public static int task;


	/* If the player doesn't move within 20 seconds, kick them */
	public static void online_check() {
		
		if (System.currentTimeMillis() < (kickAttemptFrequency * 1000) + last_request) {
			return;
		}

		last_request = System.currentTimeMillis();
		exemptPlayers.clear();

		/* Tell afk players to move */
		for (Map.Entry<UUID, Location> afkPlayer : GetAfkPlayers.playerLocations.entrySet()) {
			Player player = Bukkit.getPlayer(afkPlayer.getKey());
			if (player == null) continue;



			if (!(GetAfkPlayers.playerTimes.get(player.getUniqueId()) > 5)) {
				exemptPlayers.add(player);
				continue;
			}
			player.sendMessage(ChatColor.AQUA + "[Announcement] Please verify that you're online by moving and looking around");
			player.sendTitle(ChatColor.GOLD + "Afk Check", "Please verify that you're online", 10, 550, 20);
		}

		/* Activate in 20ish seconds */
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(AntiAfk.instance, () -> {
			for (Map.Entry<UUID, Location> afkPlayer : GetAfkPlayers.playerLocations.entrySet()) {
				Player player = Bukkit.getPlayer(afkPlayer.getKey());

				if (player == null) continue;


				if (!(GetAfkPlayers.playerHasNotMoved(player)) && !exemptPlayers.contains(player)) {
					GetAfkPlayers.playerLocations.replace(player.getUniqueId(), player.getLocation());
					player.resetTitle();
					player.sendMessage(ChatColor.GREEN + "You're no longer afk");
					GetAfkPlayers.playerTimes.replace(player.getUniqueId(), 0);
					exemptPlayers.add(player);
					continue;
				}

				if (System.currentTimeMillis() > last_request + 20000) {
					if (GetAfkPlayers.playerTimes.get(player.getUniqueId()) > 5 && !exemptPlayers.contains(player)) {
						player.kickPlayer(ChatColor.RED + "Kicked for being afk in low tps");
						AntiAfk.instance.getLogger().info(player.getDisplayName() + " was kicked for being afk");
						GetAfkPlayers.playerTimes.replace(player.getUniqueId(), 0);
					}
					Bukkit.getScheduler().cancelTask(task);
				}
			}
		}, 20, 20);
	}
}
