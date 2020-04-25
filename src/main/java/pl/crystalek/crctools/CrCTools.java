package pl.crystalek.crctools;

import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.commands.FeedCommand;
import pl.crystalek.managers.FileManager;

public final class CrCTools extends JavaPlugin {
    public static CrCTools inst;

    public CrCTools() {
        inst = this;
    }

    public static CrCTools getInst() {
        return inst;
    }

    @Override
    public void onEnable() {
        FileManager.checkFiles();
        getCommand("feed").setExecutor(new FeedCommand());
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        System.out.println();
    }
}
