package pl.crystalek.crctools;

import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crctools.commands.*;
import pl.crystalek.crctools.managers.FileManager;

public final class CrCTools extends JavaPlugin {

    @Override
    public void onEnable() {
        FileManager fileManager = new FileManager();
        fileManager.checkFiles();
        saveDefaultConfig();
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("tppos").setExecutor(new TpposCommand());
        getCommand("exp").setExecutor(new ExpCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("speed").setExecutor(new SpeedCommand());
    }

    @Override
    public void onDisable() {
        System.out.println();
    }
}
