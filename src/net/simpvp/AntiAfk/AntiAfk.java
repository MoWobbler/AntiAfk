package net.simpvp.AntiAfk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;



public class AntiAfk extends JavaPlugin implements Listener {
	
	public static JavaPlugin instance;
	public Map<Player, Long> playerLastMoveTime = new HashMap<Player, Long>();
	public static ArrayList<Player> afkPlayers = new ArrayList<>();
	
	GetTps gettps = new GetTps();
	Double min_tps;
	Integer afk_secs;
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		instance = this;
		min_tps = AntiAfk.instance.getConfig().getDouble("Minimum_tps");	
		afk_secs = AntiAfk.instance.getConfig().getInt("Afk_seconds");
		gettps.variables();	
		setPlayersAfk();
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	// This tests for afk players
    public void setPlayersAfk() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Map.Entry<Player, Long> entry : playerLastMoveTime.entrySet()) {
            	
            	// Mark player as afk
                if ((System.currentTimeMillis() - entry.getValue()) > afk_secs * 1000) {
                    Player p = entry.getKey();
                    afkPlayers.add(p);
                }
            }
            for (Player playerToRemove : afkPlayers) {
                playerLastMoveTime.remove(playerToRemove);
            }
            // Kick all afk players if tps is lower than the tps set in the config
            if (Double.parseDouble(gettps.getTPS(0)) < min_tps) {
            	for (Player p : getAfkPlayers()) {
            		p.kickPlayer("Kicked for being afk during low tps");
            	}
            }
        }, 0, 100);
    }
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
	    playerLastMoveTime.put(e.getPlayer(), System.currentTimeMillis());
	    if (afkPlayers.contains(e.getPlayer())) {
	    	afkPlayers.remove(e.getPlayer());
	    }
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
	    if (playerLastMoveTime.containsKey(e.getPlayer())) {
	        playerLastMoveTime.remove(e.getPlayer());
	    }
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		playerLastMoveTime.put(e.getPlayer(), System.currentTimeMillis());
	}
	
	public static ArrayList<Player> getAfkPlayers() {
		return afkPlayers;
	}
	
	
}
