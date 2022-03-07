package net.simpvp.AntiAfk;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class GetAfkPlayers {

    static HashMap<UUID, Location> playerLocations = new HashMap<>();
    static HashMap<UUID, Long> lastMoveTime = new HashMap<>();
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

                if (isPlayerAfk(player) && !Bukkit.getScheduler().isCurrentlyRunning(KickPlayer.task)) {
                    KickPlayer.online_check();
                    return;
                }
            }

            for (Map.Entry<UUID, Location> afkPlayer : playerLocations.entrySet()) {

                Player player = Bukkit.getPlayer(afkPlayer.getKey());

                if (player == null) {
                    continue;
                }
                playerLocations.put(player.getUniqueId(), player.getLocation());
                lastMoveTime.put(player.getUniqueId(), System.currentTimeMillis());
            }

        }


        /* Only store locations for players we want to kick */
        public static void storePlayerLocations () {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AntiAfk.kick_players.contains(player.getUniqueId())) {
                    if (!playerLocations.containsKey(player.getUniqueId())) {
                        playerLocations.put(player.getUniqueId(), player.getLocation());
                        lastMoveTime.put(player.getUniqueId(), System.currentTimeMillis());
                    }
                }
            }
        }


        /* Return true if a player is attempting to avoid afk detection */
        public static boolean playerInAfkMachine (Player player){
            Location lastLocation = playerLocations.get(player.getUniqueId());
            if (player.getLocation().getBlock().getType().equals(Material.WATER)) return true;
            if (player.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.WATER)) return true;
            if (player.getLocation().getPitch() == lastLocation.getPitch()) return true;
            if (player.getLocation().getYaw() == lastLocation.getYaw()) return true;
            return false;
        }


        /* Return true if a player is afk */
        public static boolean isPlayerAfk (Player player) {
            if (playerLocations.get(player.getUniqueId()).equals(player.getLocation())) {
                return true;
            }

            if (playerInAfkMachine(player)) {
                return true;
            }
            return false;
        }



}
