package pl.crystalek.crctools.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.exceptions.WarpExistException;
import pl.crystalek.crctools.model.Warp;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public final class WarpManager {
    private final CrCTools crCTools;
    private final DecimalFormat decimalFormat;
    private final Map<String, Warp> warpList = new HashMap<>();

    public WarpManager(final CrCTools crCTools, final DecimalFormat decimalFormat) {
        this.crCTools = crCTools;
        this.decimalFormat = decimalFormat;
    }

    public void createWarp(final String name, final Player player) throws IllegalArgumentException, WarpExistException {
        if (name.contains(".")) {
            throw new IllegalArgumentException("you cannot use a period!");
        }
        if (warpList.containsKey(name)) {
            throw new WarpExistException("warp does exist!");
        }
        final Location location = player.getLocation();
        final LocalDateTime localDateTime = LocalDateTime.now();
        final String date = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        warpList.put(name, new Warp(localDateTime, player.getName(), location));
        final FileConfiguration config = crCTools.getConfig();
        final String warpPath = "warps." + name;
        config.createSection(warpPath);
        config.set(warpPath + ".date", date);
        config.set(warpPath + ".author", player.getName());
        config.createSection(warpPath + ".location");
        config.set(warpPath + ".location.world", location.getWorld().getName());
        config.set(warpPath + ".location.x", decimalFormat.format(location.getX()));
        config.set(warpPath + ".location.y", decimalFormat.format(location.getY()));
        config.set(warpPath + ".location.z", decimalFormat.format(location.getZ()));
        crCTools.saveConfig();
    }

    public void deleteWarp(final String name) throws WarpExistException {
        if (!warpList.containsKey(name)) {
            throw new WarpExistException("warp doesn't exist!");
        }
        warpList.remove(name);
        crCTools.getConfig().set("warps." + name, null);
        crCTools.saveConfig();
    }

    public Warp getWarp(final String name) {
        return warpList.get(name);
    }

    public void loadWarps() {
        final FileConfiguration config = crCTools.getConfig();
        if (config.getConfigurationSection("warps") != null) {
            final ConfigurationSection configurationSection = config.getConfigurationSection("warps");
            final List<String> keyList = new ArrayList<>(configurationSection.getKeys(false));
            for (String string : keyList) {
                final String name = string;
                string = "warps." + string;
                warpList.put(name,
                        new Warp(LocalDateTime.parse(config.getString(string + ".date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                config.getString(string + ".author"),
                                new Location(Bukkit.getWorld(config.getString(string + ".location.world")),
                                        Double.parseDouble(config.getString(string + ".location.x")),
                                        Double.parseDouble(config.getString(string + ".location.y")),
                                        Double.parseDouble(config.getString(string + ".location.z")))));
            }
        }
    }

    public boolean printWarpList(final CommandSender sender, final String[] args, final FileManager fileManager) {
        if (args.length != 1) {
            final Set<String> strings = warpList.keySet();
            final List<String> warpList = new ArrayList<>(strings);
            if (warpList.isEmpty() || strings == null) {
                sender.sendMessage(fileManager.getMsg("warp.error"));
                return true;
            }
            final String warps = warpList.stream().collect(Collectors.joining(fileManager.getMsg("interlude")));
            sender.sendMessage(fileManager.getMsg("warp.list").replace("{WARPS}", warps));
            return true;
        }
        return false;
    }
}