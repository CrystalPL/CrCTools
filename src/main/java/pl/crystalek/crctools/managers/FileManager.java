package pl.crystalek.crctools.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.utils.ChatUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileManager {
    private final CrCTools crCTools;
    private YamlConfiguration yamlConfiguration;

    public FileManager(final CrCTools crCTools) {
        this.crCTools = crCTools;
    }

    public void checkFiles() {
        if (!(new File(crCTools.getDataFolder(), "messages.yml").exists())) {
            crCTools.saveResource("messages.yml", true);
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration(new File(crCTools.getDataFolder(), "messages.yml"));
        crCTools.reloadConfig();
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

    public boolean setValuePath(String path, String value) throws IOException {
        if (!yamlConfiguration.contains(path)) {
            return false;
        }
        if (yamlConfiguration.getStringList(path) != null) {
            final List<String> stringList = yamlConfiguration.getStringList(path);
            System.out.println(stringList.size());
            if (stringList.size() == 0) {
                yamlConfiguration.set(path, value);
                return true;
            } else {
                if (value.equals("clear")) {
                    stringList.remove(stringList.get(stringList.size() - 1));
                } else {
                    stringList.add(value);
                }
                yamlConfiguration.set(path, stringList);
                return true;
            }
        }
        yamlConfiguration.save(new File(crCTools.getDataFolder(), "messages.yml"));
        return true;
    }
}