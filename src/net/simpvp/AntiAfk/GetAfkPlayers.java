package net.simpvp.AntiAfk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public abstract class GetAfkPlayers implements Plugin {
	
	// This class labels players as afk and kicks them if tps is bad

	public static Map<Player, Long> playerLastMoveTime = new HashMap<Player, Long>();
	public static ArrayList<Player> afkPlayers = new ArrayList<>();
	
	
	// This tests for afk players
    public static void setPlayersAfk() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(AntiAfk.instance, () -> {
            for (Map.Entry<Player, Long> entry : playerLastMoveTime.entrySet()) {
            	
            	// Mark player as afk
                if ((System.currentTimeMillis() - entry.getValue()) > AntiAfk.afk_secs * 1000) {
                    Player p = entry.getKey();
                    afkPlayers.add(p);
                    p.sendMessage(ChatColor.RED + "You are now afk");
                }
            }
            for (Player playerToRemove : afkPlayers) {
                playerLastMoveTime.remove(playerToRemove);
            }
            // Kick all afk players if tps is lower than the tps set in the config
            if (Double.parseDouble(AntiAfk.gettps.getTPS(0)) < AntiAfk.min_tps && !afkPlayers.isEmpty()) {
            	KickPlayer.online_check();
            }
        }, 0, 100);
    }
}
