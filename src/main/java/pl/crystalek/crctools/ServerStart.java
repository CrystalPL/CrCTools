package pl.crystalek.crctools;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.commands.*;
import pl.crystalek.crctools.listeners.*;
import pl.crystalek.crctools.managers.*;
import pl.crystalek.crctools.tasks.AutoMessage;
import pl.crystalek.crctools.tasks.AutoSave;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ServerStart {
    private final FileManager fileManager;
    private final TpaManager tpaManager;
    private final MsgManager msgManager;
    private final UserManager userManager;
    private final DecimalFormat decimalFormat;
    private final WarpManager warpManager;
    private final PermissionManager permissionManager;
    private final MailManager mailManager;
    private final CrCTools crCTools;

    public ServerStart(final CrCTools crCTools) {
        this.crCTools = crCTools;
        this.decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        this.userManager = new UserManager(crCTools);
        this.fileManager = new FileManager(crCTools, userManager, decimalFormat);
        this.tpaManager = new TpaManager();
        this.msgManager = new MsgManager();
        this.warpManager = new WarpManager(crCTools, decimalFormat);
        this.permissionManager = new PermissionManager(fileManager, crCTools, userManager);
        this.mailManager = new MailManager(fileManager, userManager);
        fileManager.checkFiles();
        new ServerOptions(setspawnLocation(), crCTools.getConfig().getBoolean("chat"));
        registerCommand();
        registerListeners();
        permissionManager.loadGroups();
        new AutoMessage(crCTools, fileManager);
        warpManager.loadWarps();
        reloadServer();
        new AutoSave(crCTools, fileManager);
    }

    private void registerCommand() {
        crCTools.getCommand("exp").setExecutor(new ExpCommand(fileManager));
        crCTools.getCommand("feed").setExecutor(new FeedCommand(fileManager));
        crCTools.getCommand("fly").setExecutor(new FlyCommand(fileManager));
        crCTools.getCommand("heal").setExecutor(new HealCommand(fileManager));
        crCTools.getCommand("speed").setExecutor(new SpeedCommand(fileManager));
        crCTools.getCommand("tppos").setExecutor(new TpposCommand(fileManager, decimalFormat));
        crCTools.getCommand("tpa").setExecutor(new TpaCommand(fileManager, crCTools, tpaManager, userManager));
        crCTools.getCommand("tpaccept").setExecutor(new TpacceptCommand(fileManager, crCTools, tpaManager, userManager));
        crCTools.getCommand("tptoggle").setExecutor(new TptoggleCommand(fileManager, userManager));
        crCTools.getCommand("tools").setExecutor(new ToolsCommand(fileManager));
        crCTools.getCommand("tpdeny").setExecutor(new TpdenyCommand(fileManager, tpaManager, userManager));
        crCTools.getCommand("msg").setExecutor(new MsgCommand(fileManager, msgManager, userManager));
        crCTools.getCommand("r").setExecutor(new ReplyCommand(fileManager, msgManager));
        crCTools.getCommand("trybmsg").setExecutor(new TrybmsgCommand(fileManager, userManager));
        crCTools.getCommand("socialspy").setExecutor(new SocialspyCommand(fileManager, msgManager));
        crCTools.getCommand("gamemode").setExecutor(new GamemodeCommand(fileManager));
        crCTools.getCommand("repair").setExecutor(new RepairCommand(fileManager));
        crCTools.getCommand("tp").setExecutor(new TpCommand(fileManager));
        crCTools.getCommand("s").setExecutor(new SCommand(fileManager));
        crCTools.getCommand("give").setExecutor(new GiveCommand(fileManager));
        crCTools.getCommand("i").setExecutor(new ItemCommand(fileManager));
        crCTools.getCommand("ip").setExecutor(new IpCommand(fileManager));
        crCTools.getCommand("kickall").setExecutor(new KickallCommand(fileManager));
        crCTools.getCommand("god").setExecutor(new GodCommand(fileManager, userManager));
        crCTools.getCommand("whois").setExecutor(new WhoisCommand(fileManager, userManager, decimalFormat));
        crCTools.getCommand("suicide").setExecutor(new SuicideCommand(fileManager));
        crCTools.getCommand("kill").setExecutor(new KillCommand(fileManager));
        crCTools.getCommand("setwarp").setExecutor(new SetwarpCommand(fileManager, warpManager));
        crCTools.getCommand("warp").setExecutor(new WarpCommand(fileManager, warpManager, crCTools));
        crCTools.getCommand("delwarp").setExecutor(new DelwarpCommand(fileManager, warpManager));
        crCTools.getCommand("warpinfo").setExecutor(new WarpinfoCommand(fileManager, warpManager, decimalFormat));
        crCTools.getCommand("sethome").setExecutor(new SethomeCommand(fileManager, userManager, decimalFormat));
        crCTools.getCommand("home").setExecutor(new HomeCommand(fileManager, userManager, crCTools));
        crCTools.getCommand("delhome").setExecutor(new DelhomeCommand(fileManager, userManager));
        crCTools.getCommand("setspawn").setExecutor(new SetSpawnCommand(fileManager, decimalFormat, crCTools));
        crCTools.getCommand("spawn").setExecutor(new SpawnCommand(fileManager, crCTools));
        crCTools.getCommand("entity").setExecutor(new EntityCommand(fileManager));
        crCTools.getCommand("rlc").setExecutor(new ReloadCommand(fileManager, crCTools));
        crCTools.getCommand("chat").setExecutor(new ChatCommand(fileManager, crCTools));
        crCTools.getCommand("perms").setExecutor(new PermissionCommand(fileManager, permissionManager, userManager));
        crCTools.getCommand("alert").setExecutor(new AlertCommand(fileManager, crCTools));
        crCTools.getCommand("tpall").setExecutor(new TpAllCommand(fileManager));
        crCTools.getCommand("mail").setExecutor(new MailCommand(fileManager, mailManager));
        crCTools.getCommand("lore").setExecutor(new LoreCommand(fileManager));
        crCTools.getCommand("rename").setExecutor(new RenameCommand(fileManager));
        crCTools.getCommand("nick").setExecutor(new NickCommand(fileManager, userManager));
        crCTools.getCommand("tpahere").setExecutor(new TpahereCommand(fileManager, userManager, tpaManager, crCTools));
        crCTools.getCommand("handgive").setExecutor(new HandGiveCommand(fileManager));
        crCTools.getCommand("clear").setExecutor(new ClearCommand(fileManager));
        crCTools.getCommand("endersee").setExecutor(new EnderseeCommand(fileManager));
        crCTools.getCommand("invsee").setExecutor(new InvseeCommand(fileManager));
        crCTools.getCommand("color").setExecutor(new ColorCommand(fileManager, userManager));
        crCTools.getCommand("ping").setExecutor(new PingCommand(fileManager));
        crCTools.getCommand("kick").setExecutor(new KickCommand(fileManager));
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(fileManager, userManager, permissionManager, mailManager, crCTools), crCTools);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(fileManager, userManager), crCTools);
        Bukkit.getPluginManager().registerEvents(new ThunderChangeListener(), crCTools);
        Bukkit.getPluginManager().registerEvents(new WeatherChangeListener(), crCTools);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(userManager), crCTools);
        Bukkit.getPluginManager().registerEvents(new EntityTargetLivingEntityListener(userManager), crCTools);
        Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocessListener(fileManager), crCTools);
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(fileManager, permissionManager, userManager), crCTools);
    }

    private Location setspawnLocation() {
        final FileConfiguration config = crCTools.getConfig();
        try {
            return new Location(Bukkit.getWorld(config.getString("spawn.world")),
                    Double.parseDouble(config.getString("spawn.x")),
                    Double.parseDouble(config.getString("spawn.y")),
                    Double.parseDouble(config.getString("spawn.z")));
        } catch (final IllegalArgumentException | NullPointerException exception) {
            return null;
        }
    }

    private void reloadServer() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            fileManager.addConfiguration(player);
            fileManager.loadPlayer(player);
            permissionManager.loadPermission(player);
            mailManager.loadMessage(player);
        }
    }

    public void savePlayer() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            try {
                fileManager.savePlayer(player);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
