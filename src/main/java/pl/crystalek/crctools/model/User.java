package pl.crystalek.crctools.model;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User {
    private final UUID uuid;
    private final String lastName;
    private String ip;
    private boolean msg = true;
    private boolean tpa = true;
    private boolean god = false;
    private Map<String, Location> homeList = new HashMap<>();

    public User(final UUID uuid, final String lastName, final String ip, final boolean msg, final boolean tpa, final boolean god, final Map<String, Location> homeList) {
        this.uuid = uuid;
        this.lastName = lastName;
        this.ip = ip;
        this.msg = msg;
        this.tpa = tpa;
        this.god = god;
        this.homeList = homeList;
    }

    public User(final UUID uuid, final String lastName, final String ip, final boolean msg, final boolean tpa, final boolean god) {
        this.uuid = uuid;
        this.lastName = lastName;
        this.ip = ip;
        this.msg = msg;
        this.tpa = tpa;
        this.god = god;
    }

    public User(final Player player) {
        this.uuid = player.getUniqueId();
        this.lastName = player.getName();
        this.ip = player.getAddress().getAddress().getHostAddress();
    }

    public void addHome(final String name, final Location location) {
        homeList.put(name, location);
    }

    public Location getHome(final String name) {
        return homeList.get(name);
    }

    public void removeHome(final String name) {
        homeList.remove(name);
    }

    public Map<String, Location> getHome() {
        return homeList;
    }

    public String getIp() {
        return ip;
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

    public boolean isGod() {
        return god;
    }

    public void setGod(boolean god) {
        this.god = god;
    }
}