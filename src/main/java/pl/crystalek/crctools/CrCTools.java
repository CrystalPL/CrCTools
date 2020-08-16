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
    //TODO ZROBIENIE MOZLIWOSCI TELEPORTOWANIA DO GRACZA OFFLINE
    //TODO FORMAT CHATU
    //TODO ZABLOKOWANIE MOZLIWOSCI DAWANIA KROPEK DO WARPOW, HOME'OW, I INNYCH RZECZY W KTORYCH ZAPISYWANY JEST STRING DO YML'A
    //TODO DODANIE LIMITU WIADOMOSCI WYSYLANYCH/ODBIERANYCH
    //TODO DADANIE KOMENDY /LIST, KTORA POKAZUJE GRACZE UWZGLEDNIAJAC PODZIAL NA GRUPY
    //TODO DODANIE WYBORU, CZY PRZY KAZDYM WCHODZENIU NA SERWER, GRACZ MA BYC TELEPORTOWANY NA SPAWN
    //TODO DODANIE WYBORU, CZY W MAILACH PRZECZYTANE MA BYC NAPISANE TAK/NIE, CZY TRUE/FALSE
    //TODO DODANIE WYBORU, CZY MAJA BYC ZAPISYWANE DATY LOGOWAN GRACZY
    //TODO ZAPISYWANIE WSZYSTKICH IP, DAT Z JAKICH GRACZ SIE LOGUJE
    //TODO WYJEBAC ZMIENIANIE WIADOMOSCI Z FILEMANAGER DO TOOLSCOMMAND
    //TODO DODANIE WYBORU, CZY GRACZ MA BYC TELEPORTOWANY, W KTOREJ TARGET WPISAL /TPACCEPT, CZY OD RAZU DO GRACZA
    //TODO DODANIE WYBORU KOLOROW, CZASU I INNYCH PIERDOL NA ALERCIE
    //TODO ZROBIENEI KOMENDY LICZACEJ POBYT GRACZA NA SERWERZE
    //TODO PRZENIESIENIE WSZYSTKICH TAKSOW
    //TODO DODANIE KOMENDY /PING
    //TODO POPRAWIC GUI W ENDERSEE I INVSEE, NIE MOZNA DAWAC GRACZOWI PRZEDMIOTU - PACKETY
    //TODO DODANEI W CONFIGU KOLOROW, KTORE MOGA BYC UZWANE PRZEZ GRACZA PODCZAS PISANIA, USTAWIANIA KOLORU PISANIA, USTAWIANAI KOLORU NICKU
    //TODO POWIADOMIENIE O LICZBIE OTRZYMANYCH WIADOMOSCI PRZY WEJSCIU GRACZA NA SERWER
    //TODO DODANIE OPCJI W CONFIGU, CZY NA START MA BYC WYSWIETLANA ILOSC MAILI
    //TODO ZAPISYWANIE NOWEJ WARTOSCI EXPA PRZY KOMENDZIE
    //TODO ZROBIENIE KOMENDY /GC
    private FileManager fileManager;
    private TpaManager tpaManager;
    private MsgManager msgManager;
    private UserManager userManager;
    private DecimalFormat decimalFormat;
    private WarpManager warpManager;
    private PermissionManager permissionManager;
    private MailManager mailManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        decimalFormat = new DecimalFormat();
        userManager = new UserManager(this);
        fileManager = new FileManager(this, userManager, decimalFormat);
        tpaManager = new TpaManager();
        msgManager = new MsgManager();
        warpManager = new WarpManager(this, decimalFormat);
        permissionManager = new PermissionManager(fileManager, this, userManager);
        mailManager = new MailManager(fileManager, userManager);
        fileManager.checkFiles();
        registerCommand();
        registerListeners();
        permissionManager.loadGroups();
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
            } catch (final IOException exception) {
                exception.printStackTrace();
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
        getCommand("tpaccept").setExecutor(new TpacceptCommand(fileManager, this, tpaManager, userManager));
        getCommand("tptoggle").setExecutor(new TptoggleCommand(fileManager, userManager));
        getCommand("tools").setExecutor(new ToolsCommand(fileManager));
        getCommand("tpdeny").setExecutor(new TpdenyCommand(fileManager, tpaManager, userManager));
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
        getCommand("chat").setExecutor(new ChatCommand(fileManager));
        getCommand("perms").setExecutor(new PermissionCommand(fileManager, permissionManager, userManager));
        getCommand("alert").setExecutor(new AlertCommand(fileManager, this));
        getCommand("tpall").setExecutor(new TpAllCommand(fileManager));
        getCommand("mail").setExecutor(new MailCommand(fileManager, mailManager));
        getCommand("lore").setExecutor(new LoreCommand(fileManager));
        getCommand("rename").setExecutor(new RenameCommand(fileManager));
        getCommand("nick").setExecutor(new NickCommand(fileManager, userManager));
        getCommand("tpahere").setExecutor(new TpahereCommand(fileManager, userManager, tpaManager, this));
        getCommand("handgive").setExecutor(new HandGiveCommand(fileManager));
        getCommand("clear").setExecutor(new ClearCommand(fileManager));
        getCommand("endersee").setExecutor(new EnderseeCommand(fileManager));
        getCommand("invsee").setExecutor(new InvseeCommand(fileManager));
        getCommand("color").setExecutor(new ColorCommand(fileManager, userManager));
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(fileManager, userManager, permissionManager, mailManager, this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(fileManager, userManager), this);
        Bukkit.getPluginManager().registerEvents(new ThunderChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new WeatherChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(userManager), this);
        Bukkit.getPluginManager().registerEvents(new EntityTargetLivingEntityListener(userManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocessListener(fileManager), this);
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(fileManager, permissionManager, userManager), this);
    }

    private void reloadServer() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            fileManager.addConfiguration(player);
            fileManager.loadPlayer(player);
            permissionManager.loadPermission(player);
            mailManager.loadMessage(player);
        }
    }
}
