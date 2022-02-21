package net.simpvp.AntiAfk;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AfkPlayer {

    private UUID uuid;
    private Location lastLocation;
    private long lastMoveTime;
    private boolean isAfk;
    private boolean possibleKickPending;


    public AfkPlayer(UUID uuid, Location lastLocation, long lastMoveTime) {
        this.uuid = uuid;
        this.lastLocation = lastLocation;
        this.lastMoveTime = lastMoveTime;
        this.isAfk = false;
        this.possibleKickPending = false;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastMoveTime(long lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }

    public long getLastMoveTime() {
        return lastMoveTime;
    }

    public void setIsAfk(boolean isAfk) {
        this.isAfk = isAfk;
    }

    public boolean getIsAfk() {
        return isAfk;
    }

    public float getLastYaw() {
        return lastLocation.getYaw();
    }

    public float getLastPitch() {
        return lastLocation.getPitch();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void setPossibleKickPending(boolean possibleKickPending) {
        this.possibleKickPending = possibleKickPending;
    }

    public boolean getPossibleKickPending() {
        return possibleKickPending;
    }
}
