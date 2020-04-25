package pl.crystalek.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.utils.ChatUtil;

import java.io.File;

public class FileManager {
    private static final File FILE = new File(CrCTools.getInst().getDataFolder(), "messages.yml");
    private static YamlConfiguration msg;

    public static void checkFiles() {
        if (!(FILE.exists())) {
            CrCTools.getInst().saveResource("messages.yml", true);
        } else {
            msg = YamlConfiguration.loadConfiguration(FILE);
        }
    }

    public static String getMsgPermission(String path) {
        return ChatUtil.fixColor(msg.getString("messages." + path).replace("{PERMISSION}", getPermission(path)));
    }

    public static String getMsg(String path) {
        return ChatUtil.fixColor(msg.getString("messages." + path));
    }

    public static String getPermission(String path) {
        return CrCTools.getInst().getConfig().getString("config.permission." + path);
    }
}
