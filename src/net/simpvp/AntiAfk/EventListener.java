package net.simpvp.AntiAfk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
	    remove(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKicked(PlayerKickEvent e) {
	    remove(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (AntiAfk.kick_players.contains(e.getPlayer().getUniqueId())) {
			GetAfkPlayers.playerLastMoveTime.put(e.getPlayer(), System.currentTimeMillis());
			GetAfkPlayers.playerLastLocation.put(e.getPlayer(), e.getPlayer().getLocation());
		}
	}
	
	/* Remove player from both arrays */
	public static void remove(Player p) {
	    if ( GetAfkPlayers.playerLastMoveTime.containsKey(p)) {
	    	GetAfkPlayers.playerLastMoveTime.remove(p);
	    }
	    if ( GetAfkPlayers.afkPlayers.contains(p)) {
	    	GetAfkPlayers.afkPlayers.remove(p);
	    }
	    if ( GetAfkPlayers.playerLastLocation.containsKey(p)) {
	    	GetAfkPlayers.playerLastLocation.remove(p);
	    }
	}
	
}
