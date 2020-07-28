package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.UserManager;
import pl.crystalek.crctools.model.User;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public final class SethomeCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final UserManager userManager;
    private final DecimalFormat decimalFormat;

    public SethomeCommand(final FileManager fileManager, final UserManager userManager, final DecimalFormat decimalFormat) {
        this.fileManager = fileManager;
        this.userManager = userManager;
        this.decimalFormat = decimalFormat;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return true;
            }
            final Player player = (Player) sender;
            final User user = userManager.getUser(player);
            final Location location = player.getLocation();
            try {
                addLocation(args, player.getName(), location, fileManager.getPlayerFile(player.getName()));
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
            user.addHome(args[0], location);
            sender.sendMessage(fileManager.getMsg("sethome.sethome"));
        } else if (args.length == 2) {
            if (!sender.hasPermission(fileManager.getPermission("sethome.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("sethome.player"));
                return true;
            }
            try {
                fileManager.getPlayerFile(args[1]);
            } catch (final NullPointerException exception) {
                sender.sendMessage(fileManager.getMsg("cantexist"));
                return true;
            }
            final YamlConfiguration playerFile = fileManager.getPlayerFile(args[1]);
            if (Bukkit.getPlayer(args[1]) == null) {
                try {
                    addLocation(args, args[1], ((Player) sender).getLocation(), playerFile);
                } catch (final IOException exception) {
                    exception.printStackTrace();
                }
            } else {
                final Player player = Bukkit.getPlayer(args[1]);
                final User user = userManager.getUser(player);
                try {
                    addLocation(args, player.getName(), ((Player) sender).getLocation(), playerFile);
                    user.addHome(args[0], ((Player) sender).getLocation());
                } catch (final IOException exception) {
                    exception.printStackTrace();
                }
                player.sendMessage(fileManager.getMsg("sethome.sethomeplayersender").replace("{PLAYER}", sender.getName()).replace("{HOME}", args[0]));
            }
            sender.sendMessage(fileManager.getMsg("sethome.player").replace("{PLAYER}", args[1]).replace("{HOME}", args[0]));
        } else {
            sender.sendMessage(fileManager.getMsg("sethome.usage"));
        }
        return true;
    }

    private void addLocation(final String[] args, final String player, final Location location, final YamlConfiguration playerFile) throws IOException {
        final String string = "homes." + args[0];
        playerFile.set(string + ".world", location.getWorld().getName());
        playerFile.set(string + ".x", decimalFormat.format(location.getX()));
        playerFile.set(string + ".y", decimalFormat.format(location.getY()));
        playerFile.set(string + ".z", decimalFormat.format(location.getZ()));
        playerFile.save(new File(fileManager.getUsers(), player + ".yml"));
    }
}