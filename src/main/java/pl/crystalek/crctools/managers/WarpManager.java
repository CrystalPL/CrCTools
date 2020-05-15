package pl.crystalek.crctools.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.model.Warp;
import pl.crystalek.crctools.utils.ChatUtil;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class WarpManager {
    private final CrCTools crCTools;
    private final DecimalFormat decimalFormat;
    private Map<String, Warp> warpList = new HashMap<>();

    public WarpManager(final CrCTools crCTools, final DecimalFormat decimalFormat) {
        this.crCTools = crCTools;
        this.decimalFormat = decimalFormat;
    }

    public void addWarp(final String name, final Player player) {
        final Location location = player.getLocation();
        final LocalDateTime localDateTime = LocalDateTime.now();
        final String date = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        warpList.put(name, new Warp(localDateTime, player.getName(), location));
        final FileConfiguration config = crCTools.getConfig();
        config.createSection("warps." + name);
        config.set("warps." + name + ".date", date);
        config.set("warps." + name + ".author", player.getName());
        config.createSection("warps." + name + ".location");
        config.set("warps." + name + ".location.world", location.getWorld().getName());
        config.set("warps." + name + ".location.x", decimalFormat.format(location.getX()));
        config.set("warps." + name + ".location.y", decimalFormat.format(location.getY()));
        config.set("warps." + name + ".location.z", decimalFormat.format(location.getZ()));
        crCTools.saveConfig();
    }

    public void removeWarp(final String name) {
        warpList.remove(name);
        crCTools.getConfig().set("warps." + name, null);
        crCTools.saveConfig();
    }

    public Warp getWarp(final String name) {
        return warpList.get(name);
    }

    public boolean checkWarp(final String name) {
        return warpList.get(name) != null;
    }

    public void loadWarps() {
        if (crCTools.getConfig().getConfigurationSection("warps") != null) {
            final ConfigurationSection configurationSection = crCTools.getConfig().getConfigurationSection("warps");
            final List<String> keyList = new ArrayList<>(configurationSection.getKeys(false));
            final FileConfiguration config = crCTools.getConfig();
            for (String string : keyList) {
                string = "warps." + string;
                warpList.put(config.getString(string + ".name"),
                        new Warp(LocalDateTime.parse(config.getString(string + ".date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                config.getString(string + ".author"),
                                new Location(Bukkit.getWorld(config.getString(string + ".location.world")),
                                        Double.parseDouble(config.getString(string + ".location.x")),
                                        Double.parseDouble(config.getString(string + ".location.y")),
                                        Double.parseDouble(config.getString(string + ".location.z")))));
            }
        }
    }

    public boolean printWarpList(final CommandSender sender, final String[] args, final WarpManager warpManager, final FileManager fileManager) {
        if (args.length != 1) {
            final Set<String> strings = warpList.keySet();
            final List<String> warpList = new ArrayList<>(strings);
            if (warpList.isEmpty() || strings == null) {
                sender.sendMessage(fileManager.getMsg("warp.error"));
                return true;
            }
            final String warps = warpList.stream().collect(Collectors.joining(ChatUtil.fixColor("&7, &6")));
            sender.sendMessage(fileManager.getMsg("warp.list").replace("{WARPS}", warps));
            return true;
        }
        if (!warpManager.checkWarp(args[0])) {
            sender.sendMessage(fileManager.getMsg("delwarp.warpexist"));
            return true;
        }
        return false;
    }
}
