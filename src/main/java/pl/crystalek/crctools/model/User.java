package pl.crystalek.crctools.model;

import org.bukkit.entity.Player;

import java.util.UUID;

public class User {
    private final UUID uuid;
    private final String lastName;
    private String ip;
    private boolean msg = true;
    private boolean tpa = true;

    public User(final UUID uuid, final String lastName, final String ip, final boolean msg, final boolean tpa) {
        this.uuid = uuid;
        this.lastName = lastName;
        this.ip = ip;
        this.msg = msg;
        this.tpa = tpa;
    }

    public User(final Player player) {
        this.uuid = player.getUniqueId();
        this.lastName = player.getName();
        this.ip = player.getAddress().getAddress().getHostAddress();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
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

    public void setMsg(final boolean msg) {
        this.msg = msg;
    }

    public boolean isTpa() {
        return tpa;
    }

    public void setTpa(final boolean tpa) {
        this.tpa = tpa;
    }
}