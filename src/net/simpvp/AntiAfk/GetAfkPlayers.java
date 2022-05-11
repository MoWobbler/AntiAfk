package net.simpvp.AntiAfk;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class GetAfkPlayers {

    static HashMap<UUID, Location> playerLocations = new HashMap<>();
    static HashMap<UUID, Integer> playerTimes = new HashMap<>();
    static int playerActivityTask;
    static boolean createTask = true;


    /* Check for low tps every 60 seconds */
    public static void checkForLowTps() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(AntiAfk.instance, new Runnable() {
            @Override
            public void run() {
                if (GetTps.getTPS()[0] < AntiAfk.minTps && createTask) {
                    AntiAfk.instance.getLogger().info("Low tps detected, looking for afk players");
                    checkActivityScheduler();
                    createTask = false;
                }
            }
        }, 600, 600);
    }


        /* Run the checkPlayerActivity function */
        public static void checkActivityScheduler () {
            /* Check for player activity */
            playerActivityTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(AntiAfk.instance, new Runnable() {
                @Override
                public void run() {
                    checkPlayerActivity();
                    if (GetTps.getTPS()[0] > AntiAfk.minTps) {
                        Bukkit.getScheduler().cancelTask(playerActivityTask);
                        AntiAfk.instance.getLogger().info("Tps isn't low anymore. No longer looking for afk players");
                        createTask = true;
                    }
                }
            }, 200, 200);
        }

        /* Check if players are afk and try to kick them */
        public static void checkPlayerActivity () {
            storePlayerLocations();

            if (Bukkit.getScheduler().isCurrentlyRunning(KickPlayer.task)) {
                return;
            }

            for (Map.Entry<UUID, Location> afkPlayer : playerLocations.entrySet()) {

                Player player = Bukkit.getPlayer(afkPlayer.getKey());

                if (player == null) {
                    continue;
                }

                if (!playerHasNotMoved(player)) {
                    playerTimes.replace(player.getUniqueId(), 0);
                }


                if (playerHasNotMoved(player) && player.isOnline()) {
                    playerTimes.replace(player.getUniqueId(), playerTimes.get(player.getUniqueId()) + 1);
                }

                if (playerTimes.get(player.getUniqueId()) > 5 && !Bukkit.getScheduler().isCurrentlyRunning(KickPlayer.task)) {
                    KickPlayer.online_check();
                    return;
                }
            }

            for (Map.Entry<UUID, Location> afkPlayer : playerLocations.entrySet()) {

                Player player = Bukkit.getPlayer(afkPlayer.getKey());

                if (player == null) {
                    continue;
                }
                playerLocations.replace(player.getUniqueId(), player.getLocation());
            }

        }


        /* Only store locations for players we want to kick */
        public static void storePlayerLocations () {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AntiAfk.kick_players.contains(player.getUniqueId())) {
                    if (!playerLocations.containsKey(player.getUniqueId())) {
                        playerLocations.put(player.getUniqueId(), player.getLocation());
                        playerTimes.put(player.getUniqueId(), 0);
                    }
                }
            }
        }


        /* Return true if a player is afk */
        public static boolean playerHasNotMoved (Player player) {
            Location lastLocation = playerLocations.get(player.getUniqueId());
            if (player.getLocation().getPitch() == lastLocation.getPitch()) return true;
            if (player.getLocation().getYaw() == lastLocation.getYaw()) return true;
            return lastLocation.equals(player.getLocation());
        }
}
