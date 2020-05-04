package pl.crystalek.crctools.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.model.User;
import pl.crystalek.crctools.utils.ChatUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileManager {
    private final CrCTools crCTools;
    private final UserManager userManager;
    private final File file;
    private final File users;
    private YamlConfiguration yamlConfiguration;
    private YamlConfiguration yamlConfigurationUsers;

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
    }

    public void loadPlayer(final Player player) {
        yamlConfigurationUsers = YamlConfiguration.loadConfiguration(new File(users, player.getName() + ".yml"));
        userManager.addUser(player, yamlConfigurationUsers.getBoolean("msg"), yamlConfigurationUsers.getBoolean("tpa"));
    }

    public void savePlayer(final Player player) throws IOException {
        final File fileSave = new File(users, player.getName() + ".yml");
        if (!fileSave.exists()) {
            fileSave.createNewFile();
        }
        final User user = userManager.getUser(player);
        yamlConfigurationUsers = YamlConfiguration.loadConfiguration(new File(users, player.getName() + ".yml"));
        yamlConfigurationUsers.set("uuid", user.getUuid().toString());
        yamlConfigurationUsers.set("nick", user.getLastName());
        yamlConfigurationUsers.set("msg", user.isMsg());
        yamlConfigurationUsers.set("tpa", user.isTpa());
        yamlConfigurationUsers.save(fileSave);
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
}