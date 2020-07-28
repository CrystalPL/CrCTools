package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.UserManager;
import pl.crystalek.crctools.model.User;
import pl.crystalek.crctools.utils.TeleportUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class HomeCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final UserManager userManager;
    private final CrCTools crCTools;

    public HomeCommand(final FileManager fileManager, final UserManager userManager, final CrCTools crCTools) {
        this.fileManager = fileManager;
        this.userManager = userManager;
        this.crCTools = crCTools;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length <= 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return true;
            }
            final Player player = (Player) sender;
            final User user = userManager.getUser(player);
            final Map<String, Location> homeList = user.getHome();
            if (args.length == 0) {
                final List<String> nameList = new ArrayList<>(homeList.keySet());
                if (nameList.isEmpty()) {
                    sender.sendMessage(fileManager.getMsg("home.error"));
                    return true;
                }
                final String homes = nameList.stream().collect(Collectors.joining(fileManager.getMsg("interlude")));
                sender.sendMessage(fileManager.getMsg("home.list").replace("{LIST}", homes));
            } else if (args.length == 1) {
                if (user.getHome(args[0]) == null) {
                    sender.sendMessage(fileManager.getMsg("delhome.error"));
                    return true;
                }
                TeleportUtil.teleportTimer(player, homeList.get(args[0]), fileManager, crCTools);
            }
        } else if (args.length == 2) {
            if (!sender.hasPermission(fileManager.getPermission("home.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("home.player"));
                return true;
            }
            try {
                fileManager.getPlayerFile(args[1]);
            } catch (final NullPointerException exception) {
                sender.sendMessage(fileManager.getMsg("cantexist"));
                return true;
            }
            final YamlConfiguration config = fileManager.getPlayerFile(args[1]);
            final String name = "homes." + args[0];
            if (config.getString(name) == null) {
                sender.sendMessage(fileManager.getMsg("delhome.errorplayer"));
                return true;
            }
            final Location location = new Location(Bukkit.getWorld(config.getString(name + ".world")),
                    Double.parseDouble(config.getString(name + ".x")),
                    Double.parseDouble(config.getString(name + ".y")),
                    Double.parseDouble(config.getString(name + ".z")));
            TeleportUtil.teleportTimer((Player) sender, location, fileManager, crCTools);
        }
        return true;
    }
}