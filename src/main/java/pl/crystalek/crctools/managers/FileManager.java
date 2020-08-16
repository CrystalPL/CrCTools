package pl.crystalek.crctools.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.model.Mail;
import pl.crystalek.crctools.model.User;
import pl.crystalek.crctools.utils.ChatUtil;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class FileManager {
    private final CrCTools crCTools;
    private final UserManager userManager;
    private final File messagesFile;
    private final File permissionsFile;
    private final File users;
    private final Map<String, YamlConfiguration> usersConfiguration = new HashMap<>();
    private final DecimalFormat decimalFormat;
    private YamlConfiguration messagesConfiguration;
    private YamlConfiguration permissionsConfiguration;

    public FileManager(final CrCTools crCTools, final UserManager userManager, final DecimalFormat decimalFormat) {
        this.crCTools = crCTools;
        this.userManager = userManager;
        messagesFile = new File(crCTools.getDataFolder(), "messages.yml");
        permissionsFile = new File(crCTools.getDataFolder(), "permissions.yml");
        users = new File(crCTools.getDataFolder(), "users");
        this.decimalFormat = decimalFormat;
    }

    public void checkFiles() {
        if (!messagesFile.exists()) {
            crCTools.saveResource("messages.yml", true);
        }
        if (!permissionsFile.exists()) {
            crCTools.saveResource("permissions.yml", true);
        }
        if (!users.exists()) {
            users.mkdir();
        }
        messagesConfiguration = YamlConfiguration.loadConfiguration(messagesFile);
        permissionsConfiguration = YamlConfiguration.loadConfiguration(permissionsFile);
        crCTools.reloadConfig();
    }

    public void loadPlayer(final Player player) {
        final YamlConfiguration configuration = usersConfiguration.get(player.getName());
        final String nick = configuration.getString("nick");
        final String nickcolor = configuration.getString("nickcolor");
        if (configuration.getConfigurationSection("homes") == null) {
            userManager.addUser(player,
                    UUID.fromString(configuration.getString("uuid")),
                    configuration.getString("ip"),
                    configuration.getBoolean("msg"),
                    configuration.getBoolean("tpa"),
                    configuration.getBoolean("god"),
                    nick,
                    nickcolor,
                    configuration.getString("messagecolor"),
                    configuration.getStringList("groups"));
        } else {
            final List<String> keyList = new ArrayList<>(configuration.getConfigurationSection("homes").getKeys(false));
            final Map<String, Location> homes = new HashMap<>();
            for (final String s : keyList) {
                final String string = "homes." + s;
                homes.put(s, new Location(Bukkit.getWorld(configuration.getString(string + ".world")),
                        Double.parseDouble(configuration.getString(string + ".x")),
                        Double.parseDouble(configuration.getString(string + ".y")),
                        Double.parseDouble(configuration.getString(string + ".z"))));
            }
            userManager.addUser(player,
                    UUID.fromString(configuration.getString("uuid")),
                    configuration.getString("ip"),
                    configuration.getBoolean("msg"),
                    configuration.getBoolean("tpa"),
                    configuration.getBoolean("god"),
                    nick,
                    nickcolor,
                    configuration.getString("messagecolor"),
                    homes,
                    configuration.getStringList("groups"));
        }
        player.setDisplayName(ChatUtil.fixColor(nickcolor) + nick);
    }

    public void savePlayer(final Player player) throws IOException {
        final YamlConfiguration configuration = usersConfiguration.get(player.getName());
        final User user = userManager.getUser(player);
        final List<String> permissions = new ArrayList<>(user.getPermissionAttachment().getPermissions().keySet());
        final Map<UUID, List<Mail>> receivedMails = user.getReceivedMails();
        final Map<UUID, List<Mail>> sentMails = user.getSentMails();
        configuration.set("uuid", user.getUuid().toString());
        configuration.set("nick", user.getDisplayName());
        configuration.set("nickcolor", user.getNickColor());
        configuration.set("messagecolor", user.getMessageColor());
        configuration.set("msg", user.isMsg());
        configuration.set("tpa", user.isTpa());
        configuration.set("god", user.isGod());
        configuration.set("ip", user.getIp());
        configuration.set("level", player.getLevel());
        configuration.set("gamemode", player.getGameMode().name());
        final Location location = player.getLocation();
        configuration.set("location.world", location.getWorld().getName());
        configuration.set("location.x", decimalFormat.format(location.getX()));
        configuration.set("location.y", decimalFormat.format(location.getY()));
        configuration.set("location.z", decimalFormat.format(location.getZ()));
        configuration.set("health", (int) player.getHealth());
        configuration.set("food", player.getFoodLevel());
        configuration.set("permissions", permissions);
        configuration.set("groups", user.getPermissionGroups());
        final Map<String, Location> home = user.getHome();
        final List<String> keys = new ArrayList<>(home.keySet());
        if (!home.isEmpty() || !keys.isEmpty()) {
            for (String string : keys) {
                final Location homeLocation = home.get(string);
                string = "homes." + string;
                configuration.set(string + ".world", homeLocation.getWorld().getName());
                configuration.set(string + ".x", decimalFormat.format(homeLocation.getX()));
                configuration.set(string + ".y", decimalFormat.format(homeLocation.getY()));
                configuration.set(string + ".z", decimalFormat.format(homeLocation.getZ()));
            }
        }
        saveMail(receivedMails, "received", configuration);
        saveMail(sentMails, "sent", configuration);
        configuration.save(new File(users, player.getName() + ".yml"));
    }

    public YamlConfiguration getPlayerFile(final String player) {
        if (Bukkit.getPlayer(player) != null) {
            return usersConfiguration.get(Bukkit.getPlayer(player).getName());
        } else {
            final File fileSave = new File(users, player + ".yml");
            if (!fileSave.exists()) {
                throw new NullPointerException("player doesn't exist");
            }
            return YamlConfiguration.loadConfiguration(fileSave);
        }
    }

    public String getPermission(final String pathPermission) {
        return permissionsConfiguration.getString("permission." + pathPermission);
    }

    public String getMsgPermission(final String pathPermission) {
        return ChatUtil.fixColor(messagesConfiguration.getString("nopermission").replace("{PERMISSION}", getPermission(pathPermission)));
    }

    public String getMsg(final String pathMessage) {
        return ChatUtil.fixColor(messagesConfiguration.getString(pathMessage));
    }

    public List<String> getMsgList(final String pathMessage) {
        return ChatUtil.fixColor(messagesConfiguration.getStringList(pathMessage));
    }

    public int getInt(final String path) {
        return crCTools.getConfig().getInt(path);
    }

    public ConfigurationSection getConfigurationSection(final String path) {
        return messagesConfiguration.getConfigurationSection(path);
    }

    public boolean setValuePath(final String path, final String value) throws IOException {
        if (!messagesConfiguration.contains(path)) {
            return false;
        }
        if (messagesConfiguration.getStringList(path) == null) {
            return false;
        }
        final List<String> stringList = messagesConfiguration.getStringList(path);
        if (stringList.size() == 0) {
            messagesConfiguration.set(path, value);
        } else {
            if (value.equals("clear")) {
                stringList.remove(stringList.get(stringList.size() - 1));
            } else {
                stringList.add(value);
            }
            messagesConfiguration.set(path, stringList);
        }
        messagesConfiguration.save(messagesFile);
        return true;
    }

    public void addConfiguration(final Player player) {
        usersConfiguration.put(player.getName(), YamlConfiguration.loadConfiguration(new File(users, player.getName() + ".yml")));
    }

    public void removeConfiguration(final Player player) {
        usersConfiguration.remove(player.getName());
    }

    public File getUsers() {
        return users;
    }

    private void saveMail(final Map<UUID, List<Mail>> uuidListMap, final String option, final YamlConfiguration configuration) {
        String name;
        for (final UUID uuid : uuidListMap.keySet()) {
            if (Bukkit.getPlayer(uuid) != null) {
                name = Bukkit.getPlayer(uuid).getName();
            } else {
                name = Bukkit.getOfflinePlayer(uuid).getName();
            }
            for (final Mail mail : uuidListMap.get(uuid)) {
                configuration.set("mail." + option + "." + name + "." + mail.getTopic() + ".sendtime", mail.getSendMessageTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                configuration.set("mail." + option + "." + name + "." + mail.getTopic() + ".contents", mail.getMail());
                configuration.set("mail." + option + "." + name + "." + mail.getTopic() + ".read", mail.isRead());
            }
        }
    }
}