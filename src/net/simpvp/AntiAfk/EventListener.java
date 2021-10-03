package net.simpvp.AntiAfk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatColor;

public class EventListener implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (AntiAfk.kick_players.contains(e.getPlayer().getUniqueId())) {
		    GetAfkPlayers.playerLastMoveTime.put(e.getPlayer(), System.currentTimeMillis());
		    if (GetAfkPlayers.afkPlayers.contains(e.getPlayer())) {
		    	GetAfkPlayers.afkPlayers.remove(e.getPlayer());
		    	e.getPlayer().sendMessage(ChatColor.GREEN + "You are no longer now afk");
		    }
		}
	}
	
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
		}
	}
	
	// Remove player from both arrays
	private void remove(Player p) {
	    if ( GetAfkPlayers.playerLastMoveTime.containsKey(p)) {
	    	GetAfkPlayers.playerLastMoveTime.remove(p);
	    }
	    if ( GetAfkPlayers.afkPlayers.contains(p)) {
	    	GetAfkPlayers.afkPlayers.remove(p);
	    }
	}
	
}
