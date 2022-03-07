package net.simpvp.AntiAfk;

import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.Server;

public class GetTps {
    public static double[] getTPS() {
        Object server;
        Field tps;
        try {
            Server mc = Bukkit.getServer();
            Field consoleField = mc.getClass().getDeclaredField("console");
            consoleField.setAccessible(true);
            server = consoleField.get(mc);
            tps = server.getClass().getSuperclass().getDeclaredField("recentTps");
            tps.setAccessible(true);
            return (double[]) tps.get(server);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
            AntiAfk.instance.getLogger().info("Error while getting tps");
        }
        return new double[]{20, 20, 20};
    }
}
