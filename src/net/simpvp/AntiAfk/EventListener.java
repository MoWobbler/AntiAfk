package net.simpvp.AntiAfk;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {


	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (!GetAfkPlayers.AfkPlayers.containsKey(e.getPlayer().getUniqueId())) {
			return;
		}

		if (!GetAfkPlayers.AfkPlayers.get(e.getPlayer().getUniqueId()).getIsAfk()) {
			return;
		}

		AfkPlayer afkPlayer = GetAfkPlayers.AfkPlayers.get(e.getPlayer().getUniqueId());
		if (playerInAfkMachine(afkPlayer)) {
			return;
		}

		updateAfkStatus(afkPlayer);
	}


	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		GetAfkPlayers.AfkPlayers.remove(e.getPlayer().getUniqueId());
	}


	@EventHandler
	public void onPlayerKicked(PlayerKickEvent e) {
		GetAfkPlayers.AfkPlayers.remove(e.getPlayer().getUniqueId());
	}


	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (AntiAfk.kick_players.contains(e.getPlayer().getUniqueId())) {
			Player player = e.getPlayer();
			AfkPlayer afkPlayer = new AfkPlayer(player.getUniqueId(), System.currentTimeMillis(), player.getLocation().getPitch(), player.getLocation().getYaw());
			GetAfkPlayers.AfkPlayers.put(player.getUniqueId(), afkPlayer);
		}
	}


	/* Return true if a player is attempting to avoid afk detection */
	private boolean playerInAfkMachine(AfkPlayer afkPlayer) {
		if (afkPlayer.getPlayer().getLocation().getBlock().getType().equals(Material.WATER)) return true;
		if (afkPlayer.getPlayer().getLocation().add(0, 1, 0).getBlock().getType().equals(Material.WATER)) return true;
		if (afkPlayer.getPlayer().getLocation().getPitch() == afkPlayer.getLastPitch()) return true;
		if (afkPlayer.getPlayer().getLocation().getYaw() == afkPlayer.getLastYaw()) return true;
		return false;
	}


	/* Update player data and inform them that they aren't afk */
	private void updateAfkStatus(AfkPlayer afkPlayer) {
		Player player = afkPlayer.getPlayer();

		afkPlayer.setLastPitch(player.getLocation().getPitch());
		afkPlayer.setLastYaw(player.getLocation().getYaw());
		afkPlayer.setLastMoveTime(System.currentTimeMillis());
		afkPlayer.setIsAfk(false);
		player.resetTitle();

		if (afkPlayer.getPossibleKickPending()) {
			player.sendMessage(ChatColor.GREEN + "You are no longer afk");
			afkPlayer.setPossibleKickPending(false);
		}
	}
}
