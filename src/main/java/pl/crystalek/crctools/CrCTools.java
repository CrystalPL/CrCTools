package pl.crystalek.crctools;

import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crctools.commands.ExpCommand;
import pl.crystalek.crctools.commands.FeedCommand;
import pl.crystalek.crctools.commands.HealCommand;
import pl.crystalek.crctools.commands.TpposCommand;
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
    }

    @Override
    public void onDisable() {
        System.out.println();
    }
}
