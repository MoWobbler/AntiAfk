package net.simpvp.AntiAfk;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AfkPlayer {

    private final UUID uuid;
    private long lastMoveTime;
    private boolean isAfk;
    private boolean possibleKickPending;

    private float lastPitch;
    private float lastYaw;


    public AfkPlayer(UUID uuid, long lastMoveTime, float lastPitch, float lastYaw) {
        this.uuid = uuid;
        this.lastMoveTime = lastMoveTime;
        this.lastPitch = lastPitch;
        this.lastYaw = lastYaw;
        this.isAfk = false;
        this.possibleKickPending = false;
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

    public float getLastPitch() {
        return lastPitch;
    }

    public void setLastPitch(float lastPitch) {
        this.lastPitch = lastPitch;
    }

    public float getLastYaw() {
        return lastYaw;
    }

    public void setLastYaw(float lastYaw) {
        this.lastYaw = lastYaw;
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
