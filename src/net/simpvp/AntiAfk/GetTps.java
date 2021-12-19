package net.simpvp.AntiAfk;

import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.Server;

public class GetTps {
    public double[] getTPS() {
        Object server = null;
        Field tps = null;
        try {
            if (server == null) {
                Server mc = Bukkit.getServer();

                Field consoleField = mc.getClass().getDeclaredField("console");
                consoleField.setAccessible(true);
                server = consoleField.get(mc);
            }
            if (tps == null) {
                tps = server.getClass().getSuperclass().getDeclaredField("recentTps");
                tps.setAccessible(true);
            }
            return (double[]) tps.get(server);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {

        }
        return new double[]{20, 20, 20};
    }
}
