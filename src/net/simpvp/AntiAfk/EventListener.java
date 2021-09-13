package net.simpvp.AntiAfk;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatColor;

public class EventListener implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
	    GetAfkPlayers.playerLastMoveTime.put(e.getPlayer(), System.currentTimeMillis());
	    if ( GetAfkPlayers.afkPlayers.contains(e.getPlayer())) {
	    	GetAfkPlayers.afkPlayers.remove(e.getPlayer());
	    	e.getPlayer().sendMessage(ChatColor.GREEN + "You are no longer now afk");
	    }
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
	    if ( GetAfkPlayers.playerLastMoveTime.containsKey(e.getPlayer())) {
	    	GetAfkPlayers.playerLastMoveTime.remove(e.getPlayer());
	    }
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		GetAfkPlayers.playerLastMoveTime.put(e.getPlayer(), System.currentTimeMillis());
	}
	
}
