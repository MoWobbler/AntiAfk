package net.simpvp.AntiAfk;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class GetAfkPlayers {

	/* This class labels players as afk and kicks them if tps is bad */
    public static HashMap<UUID, AfkPlayer> AfkPlayers = new HashMap<>();


	/* This tests for afk players */
    public static void checkForAfkPlayers() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(AntiAfk.instance, new Runnable() {
            @Override
            public void run() {
                checkActivity();
            }
        }, 0, AntiAfk.activityCheckFrequency * 20);
    }


    /* Decide if a player should be labeled as afk and try to kick them */
    public static void checkActivity() {
        for (Map.Entry<UUID, AfkPlayer> afkPlayerEntry : AfkPlayers.entrySet()) {
            AfkPlayer afkPlayer = afkPlayerEntry.getValue();

            /* Mark player as afk */
            if ((System.currentTimeMillis() - afkPlayer.getLastMoveTime()) > AntiAfk.afkSecs * 1000) {
                if (!afkPlayer.getIsAfk()) {
                    afkPlayer.setIsAfk(true);
                }
                /* Start an afk check for players if tps is lower than the tps set in the config */
                else if (GetTps.getTPS()[0] < AntiAfk.minTps) {
                    KickPlayer.online_check(afkPlayer);
                }
            }
        }
    }
}
