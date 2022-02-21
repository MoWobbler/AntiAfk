package net.simpvp.AntiAfk;

import org.bukkit.ChatColor;
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
		if (!GetAfkPlayers.AfkPlayers.containsKey(e.getPlayer().getUniqueId())) return;
		if (!GetAfkPlayers.AfkPlayers.get(e.getPlayer().getUniqueId()).getIsAfk()) return;

		AfkPlayer afkPlayer = GetAfkPlayers.AfkPlayers.get(e.getPlayer().getUniqueId());
		afkPlayer.setLastMoveTime(System.currentTimeMillis());
		afkPlayer.setLastLocation(afkPlayer.getPlayer().getLocation());
		afkPlayer.setIsAfk(false);
		e.getPlayer().resetTitle();

		if (afkPlayer.getPossibleKickPending()) {
			e.getPlayer().sendMessage(ChatColor.GREEN + "You are no longer afk");
			afkPlayer.setPossibleKickPending(false);
		}
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
			AfkPlayer afkPlayer = new AfkPlayer(player.getUniqueId(), player.getLocation().add(1,0,0), System.currentTimeMillis());
			GetAfkPlayers.AfkPlayers.put(player.getUniqueId(), afkPlayer);
		}
	}
}
