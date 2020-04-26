package pl.crystalek.crctools.managers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import pl.crystalek.crctools.utils.ChatUtil;

import java.io.File;
import java.util.List;

public class FileManager {
    private final static Plugin crCTools = Bukkit.getServer().getPluginManager().getPlugin("CrCTools");
    private final File FILE = new File(crCTools.getDataFolder(), "messages.yml");
    private static YamlConfiguration yamlConfiguration;

    public FileManager() {
    }

    public void checkFiles() {
        if (!(FILE.exists())) {
            crCTools.saveResource("messages.yml", true);
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration(FILE);
    }

    public String getPermission(final String pathPermission) {
        return crCTools.getConfig().getString("config.permission." + pathPermission);
    }

    public String getMsgPermission(final String pathPermission) {
        return ChatUtil.fixColor(yamlConfiguration.getString("messages.nopermission").replace("{PERMISSION}", getPermission(pathPermission)));
    }

    public String getMsg(final String pathMessage) {
        return ChatUtil.fixColor(yamlConfiguration.getString("messages." + pathMessage));
    }

    public List<String> getMsgList(String pathMessage) {
        return ChatUtil.fixColor(yamlConfiguration.getStringList("messages." + pathMessage));
    }
}
