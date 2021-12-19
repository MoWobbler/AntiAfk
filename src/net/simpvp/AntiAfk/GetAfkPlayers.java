package net.simpvp.AntiAfk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class GetAfkPlayers implements Plugin {
	
	/* This class labels players as afk and kicks them if tps is bad */

	/* Players are added to playerLastMoveTime when they join or when they do /on */
	public static Map<Player, Location> playerLastLocation = new HashMap<Player, Location>();
	public static Map<Player, Long> playerLastMoveTime = new HashMap<Player, Long>();
	public static ArrayList<Player> afkPlayers = new ArrayList<>();
	
	
	/* This tests for afk players */
    public static void setPlayersAfk() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(AntiAfk.instance, () -> {
       	
            for (Map.Entry<Player, Long> entry : playerLastMoveTime.entrySet()) {
            	
            	Player p = entry.getKey();
            	
            	/* Update map if player has moved*/    	
            	if (!p.getLocation().equals(playerLastLocation.get(p))) {
            		playerLastLocation.put(p, p.getLocation());
            		playerLastMoveTime.put(p, System.currentTimeMillis());
            		continue;
            	}
            	
            	/* Mark player as afk */
                if ((System.currentTimeMillis() - entry.getValue()) > AntiAfk.afk_secs * 1000) {
                    afkPlayers.add(p);
                }
            }
            for (Player playerToRemove : afkPlayers) {
                playerLastMoveTime.remove(playerToRemove);
                playerLastLocation.remove(playerToRemove);
            }
            /* Start an afk check for players if tps is lower than the tps set in the config */
            if (AntiAfk.gettps.getTPS()[0] < AntiAfk.min_tps && !afkPlayers.isEmpty()) {
         		Player p;
         		for (Iterator<?> afkListIterator = AntiAfk.instance.getServer().getOnlinePlayers().iterator(); afkListIterator.hasNext();) {
         			p = (Player) afkListIterator.next();
         			if (GetAfkPlayers.isAfk(p)) {
         				KickPlayer.online_check();
         			}
        		}
            }
        }, 0, AntiAfk.scheduler_seconds * 20);
    }
    
    /* return true if player is afk. Used when kicking players in KickPlayer */
    public static boolean isAfk(Player player) {
    	if (afkPlayers.contains(player)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
}
