package pl.crystalek.crctools.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import pl.crystalek.crctools.CrCTools;

import java.util.*;

public class User {
    private final UUID uuid;
    private final String ip;
    private final PermissionAttachment permissionAttachment;
    private final Map<UUID, Boolean> tpaList = new HashMap<>();
    private String displayName;
    private String nickColor;
    private String messageColor;
    private boolean msg = true;
    private boolean tpa = true;
    private boolean god = false;
    private Map<UUID, List<Mail>> receivedMails = new HashMap<>();
    private Map<UUID, List<Mail>> sentMails = new HashMap<>();
    private Map<String, Location> homeList = new HashMap<>();
    private List<String> permissionGroups = new ArrayList<>();

    public User(final UUID uuid, final String ip, final boolean msg, final boolean tpa, final boolean god, final String displayName, final String nickColor, final String messageColor, final Map<String, Location> homeList, final CrCTools crCTools, final List<String> listGroups) {
        this.uuid = uuid;
        this.ip = ip;
        this.msg = msg;
        this.tpa = tpa;
        this.god = god;
        this.displayName = displayName;
        this.nickColor = nickColor;
        this.messageColor = messageColor;
        this.homeList = homeList;
        this.permissionGroups = listGroups;
        this.permissionAttachment = Bukkit.getPlayer(uuid).addAttachment(crCTools);
    }

    public User(final UUID uuid, final String ip, final boolean msg, final boolean tpa, final boolean god, final CrCTools crCTools, final String displayName, final String nickColor, final String messageColor, final List<String> listGroups) {
        this.uuid = uuid;
        this.ip = ip;
        this.msg = msg;
        this.tpa = tpa;
        this.god = god;
        this.displayName = displayName;
        this.nickColor = nickColor;
        this.messageColor = messageColor;
        this.permissionAttachment = Bukkit.getPlayer(uuid).addAttachment(crCTools);
        this.permissionGroups = listGroups;
    }

    public User(final Player player, final CrCTools crCTools) {
        this.uuid = player.getUniqueId();
        this.ip = player.getAddress().getAddress().getHostAddress();
        this.permissionAttachment = player.addAttachment(crCTools);
        this.displayName = player.getName();
        final FileConfiguration config = crCTools.getConfig();
        this.nickColor = config.getString("defaultnickcolor");
        this.messageColor = config.getString("defaultmessagecolor");
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getNickColor() {
        return nickColor;
    }

    public void setNickColor(final String nickColor) {
        this.nickColor = nickColor;
    }

    public String getMessageColor() {
        return messageColor;
    }

    public void setMessageColor(final String messageColor) {
        this.messageColor = messageColor;
    }

    public Map<UUID, Boolean> getTpaList() {
        return tpaList;
    }

    public Map<UUID, List<Mail>> getReceivedMails() {
        return receivedMails;
    }

    public void setReceivedMails(final Map<UUID, List<Mail>> receivedMails) {
        this.receivedMails = receivedMails;
    }

    public Map<UUID, List<Mail>> getSentMails() {
        return sentMails;
    }

    public void setSentMails(final Map<UUID, List<Mail>> sentMails) {
        this.sentMails = sentMails;
    }

    public void addHome(final String name, final Location location) {
        this.homeList.put(name, location);
    }

    public Location getHome(final String name) {
        return this.homeList.get(name);
    }

    public void removeHome(final String name) {
        this.homeList.remove(name);
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

    public PermissionAttachment getPermissionAttachment() {
        return permissionAttachment;
    }

    public List<String> getPermissionGroups() {
        return permissionGroups;
    }
}