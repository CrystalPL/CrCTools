package pl.crystalek.crctools;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crctools.commands.*;
import pl.crystalek.crctools.listeners.*;
import pl.crystalek.crctools.managers.*;
import pl.crystalek.crctools.tasks.AutoMessage;
import pl.crystalek.crctools.tasks.AutoSave;

import java.io.IOException;
import java.text.DecimalFormat;

public final class CrCTools extends JavaPlugin {
    private FileManager fileManager;
    private TpaManager tpaManager;
    private MsgManager msgManager;
    private UserManager userManager;
    private DecimalFormat decimalFormat;
    private WarpManager warpManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        decimalFormat = new DecimalFormat();
        userManager = new UserManager();
        fileManager = new FileManager(this, userManager, decimalFormat);
        tpaManager = new TpaManager();
        msgManager = new MsgManager();
        warpManager = new WarpManager(this, decimalFormat);
        fileManager.checkFiles();
        registerCommand();
        registerListeners();
        new AutoMessage(this, fileManager);
        warpManager.loadWarps();
        reloadServer();
        new AutoSave(this, fileManager);
    }

    @Override
    public void onDisable() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            try {
                fileManager.savePlayer(player);
            } catch (IOException ignored) {
            }
        }
    }

    private void registerCommand() {
        getCommand("exp").setExecutor(new ExpCommand(fileManager));
        getCommand("feed").setExecutor(new FeedCommand(fileManager));
        getCommand("fly").setExecutor(new FlyCommand(fileManager));
        getCommand("heal").setExecutor(new HealCommand(fileManager));
        getCommand("speed").setExecutor(new SpeedCommand(fileManager));
        getCommand("tppos").setExecutor(new TpposCommand(fileManager, decimalFormat));
        getCommand("tpa").setExecutor(new TpaCommand(fileManager, this, tpaManager, userManager));
        getCommand("tpaccept").setExecutor(new TpacceptCommand(fileManager, this, tpaManager));
        getCommand("tptoggle").setExecutor(new TptoggleCommand(fileManager, userManager));
        getCommand("tools").setExecutor(new ToolsCommand(fileManager));
        getCommand("tpdeny").setExecutor(new TpdenyCommand(fileManager, tpaManager));
        getCommand("msg").setExecutor(new MsgCommand(fileManager, msgManager, userManager));
        getCommand("r").setExecutor(new ReplyCommand(fileManager, msgManager));
        getCommand("trybmsg").setExecutor(new TrybmsgCommand(fileManager, userManager));
        getCommand("socialspy").setExecutor(new SocialspyCommand(fileManager, msgManager));
        getCommand("gamemode").setExecutor(new GamemodeCommand(fileManager));
        getCommand("repair").setExecutor(new RepairCommand(fileManager));
        getCommand("tp").setExecutor(new TpCommand(fileManager));
        getCommand("s").setExecutor(new SCommand(fileManager));
        getCommand("give").setExecutor(new GiveCommand(fileManager));
        getCommand("i").setExecutor(new ItemCommand(fileManager));
        getCommand("ip").setExecutor(new IpCommand(fileManager));
        getCommand("kickall").setExecutor(new KickallCommand(fileManager));
        getCommand("god").setExecutor(new GodCommand(fileManager, userManager));
        getCommand("whois").setExecutor(new WhoisCommand(fileManager, userManager, decimalFormat));
        getCommand("suicide").setExecutor(new SuicideCommand(fileManager));
        getCommand("kill").setExecutor(new KillCommand(fileManager));
        getCommand("setwarp").setExecutor(new SetwarpCommand(fileManager, warpManager));
        getCommand("warp").setExecutor(new WarpCommand(fileManager, warpManager, this));
        getCommand("delwarp").setExecutor(new DelwarpCommand(fileManager, warpManager));
        getCommand("warpinfo").setExecutor(new WarpinfoCommand(fileManager, warpManager, decimalFormat));
        getCommand("sethome").setExecutor(new SethomeCommand(fileManager, userManager, decimalFormat));
        getCommand("home").setExecutor(new HomeCommand(fileManager, userManager, this));
        getCommand("delhome").setExecutor(new DelhomeCommand(fileManager, userManager));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(fileManager, decimalFormat, this));
        getCommand("spawn").setExecutor(new SpawnCommand(fileManager, this));
        getCommand("entity").setExecutor(new EntityCommand(fileManager));
        getCommand("rlc").setExecutor(new ReloadCommand(fileManager, this));
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(fileManager, userManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(fileManager, userManager), this);
        Bukkit.getPluginManager().registerEvents(new ThunderChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new WeatherChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(userManager), this);
        Bukkit.getPluginManager().registerEvents(new EntityTargetLivingEntityListener(userManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocessListener(fileManager), this);
    }

    private void reloadServer() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            fileManager.addConfiguration(player);
            fileManager.loadPlayer(player);
        }
    }
}
