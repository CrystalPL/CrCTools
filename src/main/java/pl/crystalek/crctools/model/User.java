package pl.crystalek.crctools.model;

import org.bukkit.entity.Player;

import java.util.UUID;

public class User {
    private final UUID uuid;
    private final String lastName;
    private boolean msg = true;
    private boolean tpa = true;

    public User(Player player) {
        this.uuid = player.getUniqueId();
        this.lastName = player.getName();
    }

    public User(Player player, boolean msg, boolean tpa) {
        this.uuid = player.getUniqueId();
        this.lastName = player.getName();
        this.msg = msg;
        this.tpa = tpa;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isMsg() {
        return msg;
    }

    public void setMsg(boolean msg) {
        this.msg = msg;
    }

    public boolean isTpa() {
        return tpa;
    }

    public void setTpa(boolean tpa) {
        this.tpa = tpa;
    }
}
