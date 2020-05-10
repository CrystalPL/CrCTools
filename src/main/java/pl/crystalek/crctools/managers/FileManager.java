package pl.crystalek.crctools.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.model.User;
import pl.crystalek.crctools.utils.ChatUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileManager {
    private final CrCTools crCTools;
    private final UserManager userManager;
    private final File file;
    private final File users;
    private YamlConfiguration yamlConfiguration;
    private Map<String, YamlConfiguration> usersConfiguration = new HashMap<>();

    public FileManager(final CrCTools crCTools, final UserManager userManager) {
        this.crCTools = crCTools;
        this.userManager = userManager;
        file = new File(crCTools.getDataFolder(), "messages.yml");
        users = new File(crCTools.getDataFolder(), "users");
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
        YamlConfiguration file = usersConfiguration.get(player.getName());
        userManager.addUser(player,
                UUID.fromString(file.getString("uuid")),
                file.getString("nick"),
                file.getString("ip"),
                file.getBoolean("msg"),
                file.getBoolean("tpa"));
    }

    public void savePlayer(final Player player) throws IOException {
        YamlConfiguration file = usersConfiguration.get(player.getName());
        final User user = userManager.getUser(player);
        file.set("uuid", user.getUuid().toString());
        file.set("nick", user.getLastName());
        file.set("msg", user.isMsg());
        file.set("tpa", user.isTpa());
        file.set("ip", user.getIp());
        file.save(new File(users, player + ".yml"));
    }

    public String getIp(final String player) {
        final File fileSave = new File(users, player + ".yml");
        if (!fileSave.exists()) {
            throw new NullPointerException("player doesn't exist");
        }
        YamlConfiguration file = usersConfiguration.get(player);
        return file.getString("ip");
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
        usersConfiguration.put(player.getName(), YamlConfiguration.loadConfiguration(new File(users, player + ".yml")));
    }

    public void removeConfiguration(final Player player) {
        usersConfiguration.remove(player.getName());
    }
}