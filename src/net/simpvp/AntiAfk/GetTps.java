package net.simpvp.AntiAfk;

import org.bukkit.Bukkit;

public class GetTps {
    public static double[] getTPS() {
        return Bukkit.getServer().getTPS();
    }
}
