package pl.crystalek.crctools;

import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crctools.commands.*;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.ChatUtil;

public final class CrCTools extends JavaPlugin {
    private FileManager fileManager;
    private ChatUtil chatUtil;

    @Override
    public void onEnable() {
        chatUtil = new ChatUtil();
        fileManager = new FileManager(this);
        saveDefaultConfig();
        fileManager.checkFiles();
        registerCommand();
    }

    @Override
    public void onDisable() {
        System.out.println();
    }

    private void registerCommand() {
        getCommand("exp").setExecutor(new ExpCommand(fileManager));
        getCommand("feed").setExecutor(new FeedCommand(fileManager));
        getCommand("fly").setExecutor(new FlyCommand(fileManager));
        getCommand("heal").setExecutor(new ExpCommand(fileManager));
        getCommand("speed").setExecutor(new SpeedCommand(fileManager));
        getCommand("tppos").setExecutor(new TpposCommand(fileManager));
        getCommand("tpa").setExecutor(new TpaCommand(fileManager, this));
        getCommand("tpaccept").setExecutor(new TpacceptCommand(fileManager, this));
        getCommand("tools").setExecutor(new ToolsCommand(fileManager));
        getCommand("tpdeny").setExecutor(new TpdenyCommand(fileManager));
    }


}
