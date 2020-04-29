package pl.crystalek.crctools.model;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class User {
    private final UUID uuid;
    private boolean fly;
    private GameMode gameMode;
    private String ip;
    private String lastName;

    public User(Player player) {
        this.uuid = player.getUniqueId();
        this.fly = player.getAllowFlight();
        this.gameMode = player.getGameMode();
        this.ip = player.getAddress().getHostString();
        this.lastName = player.getName();
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isFly() {
        return fly;
    }

    public void setFly(boolean fly) {
        this.fly = fly;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
