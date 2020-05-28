package pl.crystalek.crctools.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.model.User;
import pl.crystalek.crctools.utils.ChatUtil;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class FileManager {
    private final CrCTools crCTools;
    private final UserManager userManager;
    private final File file;
    private final File users;
    private final Map<String, YamlConfiguration> usersConfiguration = new HashMap<>();
    private final DecimalFormat decimalFormat;
    private YamlConfiguration yamlConfiguration;

    public FileManager(final CrCTools crCTools, final UserManager userManager, final DecimalFormat decimalFormat) {
        this.crCTools = crCTools;
        this.userManager = userManager;
        file = new File(crCTools.getDataFolder(), "messages.yml");
        users = new File(crCTools.getDataFolder(), "users");
        this.decimalFormat = decimalFormat;
    }

    public void checkFiles() {
        if (!file.exists()) {
            crCTools.saveResource("messages.yml", true);
        }
        if (!users.exists()) {
            users.mkdir();
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        crCTools.reloadConfig();
    }

    public void loadPlayer(final Player player) {
        final YamlConfiguration configuration = usersConfiguration.get(player.getName());
        if (configuration.getConfigurationSection("homes") == null) {
            userManager.addUser(player,
                    UUID.fromString(configuration.getString("uuid")),
                    configuration.getString("nick"),
                    configuration.getString("ip"),
                    configuration.getBoolean("msg"),
                    configuration.getBoolean("tpa"),
                    configuration.getBoolean("god"));
        } else {
            final List<String> keyList = new ArrayList<>(configuration.getConfigurationSection("homes").getKeys(false));
            Map<String, Location> homes = new HashMap<>();
            for (String s : keyList) {
                String string = "homes." + s;
                homes.put(s, new Location(Bukkit.getWorld(configuration.getString(string + ".world")),
                        Double.parseDouble(configuration.getString(string + ".x")),
                        Double.parseDouble(configuration.getString(string + ".y")),
                        Double.parseDouble(configuration.getString(string + ".z"))));
            }
            userManager.addUser(player,
                    UUID.fromString(configuration.getString("uuid")),
                    configuration.getString("nick"),
                    configuration.getString("ip"),
                    configuration.getBoolean("msg"),
                    configuration.getBoolean("tpa"),
                    configuration.getBoolean("god"),
                    homes);
        }
    }

    public YamlConfiguration getPlayerConfiguration(final Player player) {
        return usersConfiguration.get(player.getName());
    }

    public void savePlayer(final Player player) throws IOException {
        final YamlConfiguration configuration = usersConfiguration.get(player.getName());
        final User user = userManager.getUser(player);
        configuration.set("uuid", user.getUuid().toString());
        configuration.set("nick", user.getLastName());
        configuration.set("msg", user.isMsg());
        configuration.set("tpa", user.isTpa());
        configuration.set("god", user.isGod());
        configuration.set("ip", user.getIp());
        configuration.set("level", player.getLevel());
        configuration.set("gamemode", player.getGameMode().name());
        final Location location = player.getLocation();
        configuration.set("location.world", location.getWorld().getName());
        configuration.set("location.x", decimalFormat.format(location.getX()));
        configuration.set("location.y", decimalFormat.format(location.getY()));
        configuration.set("location.z", decimalFormat.format(location.getZ()));
        configuration.set("health", (int) player.getHealth());
        configuration.set("food", player.getFoodLevel());
        final Map<String, Location> home = user.getHome();
        final List<String> keys = new ArrayList<>(home.keySet());
        if (!home.isEmpty() || !keys.isEmpty()) {
            for (String string : keys) {
                final Location homeLocation = home.get(string);
                string = "homes." + string;
                configuration.set(string + ".world", homeLocation.getWorld().getName());
                configuration.set(string + ".x", decimalFormat.format(homeLocation.getX()));
                configuration.set(string + ".y", decimalFormat.format(homeLocation.getY()));
                configuration.set(string + ".z", decimalFormat.format(homeLocation.getZ()));
            }
        }
        configuration.save(new File(users, player.getName() + ".yml"));
    }

    public YamlConfiguration getPlayerFile(final String player) {
        final File fileSave = new File(users, player + ".yml");
        if (!fileSave.exists()) {
            throw new NullPointerException("player doesn't exist");
        }
        return YamlConfiguration.loadConfiguration(fileSave);
    }

    public String getPermission(final String pathPermission) {
        return crCTools.getConfig().getString("permission." + pathPermission);
    }

    public String getMsgPermission(final String pathPermission) {
        return ChatUtil.fixColor(yamlConfiguration.getString("nopermission").replace("{PERMISSION}", getPermission(pathPermission)));
    }

    public String getMsg(final String pathMessage) {
        return ChatUtil.fixColor(yamlConfiguration.getString(pathMessage));
    }

    public List<String> getMsgList(final String pathMessage) {
        return ChatUtil.fixColor(yamlConfiguration.getStringList(pathMessage));
    }

    public int getInt(final String path) {
        return crCTools.getConfig().getInt(path);
    }

    public ConfigurationSection getConfigurationSection(final String path) {
        return yamlConfiguration.getConfigurationSection(path);
    }

    public boolean setValuePath(final String path, final String value) throws IOException {
        if (!yamlConfiguration.contains(path)) {
            return false;
        }
        if (yamlConfiguration.getStringList(path) == null) {
            return false;
        }
        final List<String> stringList = yamlConfiguration.getStringList(path);
        if (stringList.size() == 0) {
            yamlConfiguration.set(path, value);
        } else {
            if (value.equals("clear")) {
                stringList.remove(stringList.get(stringList.size() - 1));
            } else {
                stringList.add(value);
            }
            yamlConfiguration.set(path, stringList);
        }
        yamlConfiguration.save(file);
        return true;
    }

    public void addConfiguration(final Player player) {
        usersConfiguration.put(player.getName(), YamlConfiguration.loadConfiguration(new File(users, player.getName() + ".yml")));
    }

    public void removeConfiguration(final Player player) {
        usersConfiguration.remove(player.getName());
    }

    public File getUsers() {
        return users;
    }
}