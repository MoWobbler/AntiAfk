package net.simpvp.AntiAfk;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;

public class GetTps {

	private final String name = Bukkit.getServer().getClass().getPackage().getName();
    private final String version = name.substring(name.lastIndexOf('.') + 1);

    private final DecimalFormat format = new DecimalFormat("##.##");

    private Object serverInstance;
    private Field tpsField;
    
    // This class gets the current TPS
    // Stolen from here: https://www.spigotmc.org/threads/get-server-tps.152860/
    
    private Class<?> getNMSClass(String className) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    void variables() {
        try {
            serverInstance = getNMSClass("MinecraftServer").getMethod("getServer").invoke(null);
            tpsField = serverInstance.getClass().getField("recentTps");
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public String getTPS(int time) {
        try {
            double[] tps = ((double[]) tpsField.get(serverInstance));
            return format.format(tps[time]);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
	
	
}
