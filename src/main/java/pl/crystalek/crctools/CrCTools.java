package pl.crystalek.crctools;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crctools.commands.*;
import pl.crystalek.crctools.listeners.PlayerJoinListener;
import pl.crystalek.crctools.listeners.PlayerQuitListener;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.model.MsgManager;
import pl.crystalek.crctools.model.UserManager;
import pl.crystalek.crctools.utils.TpaUtil;

import java.io.IOException;

public final class CrCTools extends JavaPlugin {
    private FileManager fileManager;
    private TpaUtil tpaUtil;
    private MsgManager msgManager;
    private UserManager userManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        userManager = new UserManager();
        fileManager = new FileManager(this, userManager);
        tpaUtil = new TpaUtil();
        msgManager = new MsgManager();
        fileManager.checkFiles();
        registerCommand();
        registerListeners();
    }

    @Override
    public void onDisable() {
        try {
            savePlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePlayer() throws IOException {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            fileManager.savePlayer(onlinePlayer);
        }
    }

    private void registerCommand() {
        getCommand("exp").setExecutor(new ExpCommand(fileManager));
        getCommand("feed").setExecutor(new FeedCommand(fileManager));
        getCommand("fly").setExecutor(new FlyCommand(fileManager));
        getCommand("heal").setExecutor(new ExpCommand(fileManager));
        getCommand("speed").setExecutor(new SpeedCommand(fileManager));
        getCommand("tppos").setExecutor(new TpposCommand(fileManager));
        getCommand("tpa").setExecutor(new TpaCommand(fileManager, this, tpaUtil));
        getCommand("tpaccept").setExecutor(new TpacceptCommand(fileManager, this, tpaUtil));
        getCommand("tools").setExecutor(new ToolsCommand(fileManager));
        getCommand("tpdeny").setExecutor(new TpdenyCommand(fileManager, tpaUtil));
        getCommand("msg").setExecutor(new MsgCommand(fileManager, msgManager, userManager));
        getCommand("r").setExecutor(new ReplyCommand(fileManager, msgManager));
        getCommand("trybmsg").setExecutor(new TrybmsgCommand(fileManager, userManager));
        getCommand("socialspy").setExecutor(new SocialspyCommand(fileManager, msgManager));
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(fileManager, userManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(fileManager, userManager), this);
    }
}
