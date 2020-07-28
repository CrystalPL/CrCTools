package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
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

public final class DelhomeCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final UserManager userManager;

    public DelhomeCommand(final FileManager fileManager, final UserManager userManager) {
        this.fileManager = fileManager;
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (args.length == 1) {
            final Player player = (Player) sender;
            final User user = userManager.getUser(player);
            if (user.getHome(args[0]) == null) {
                sender.sendMessage(fileManager.getMsg("delhome.error"));
                return true;
            }
            user.removeHome(args[0]);
            final YamlConfiguration playerFile = fileManager.getPlayerFile(player.getName());
            playerFile.set("homes." + args[0], null);
            sender.sendMessage(fileManager.getMsg("delhome.delhome"));
            try {
                playerFile.save(new File(fileManager.getUsers(), player.getName() + ".yml"));
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        } else if (args.length == 2) {
            if (!sender.hasPermission(fileManager.getPermission("delhome.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("delhome.player"));
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
                if (playerFile.getString("homes." + args[1]) == null) {
                    sender.sendMessage(fileManager.getMsg("delhome.errorplayer"));
                    return true;
                }
                playerFile.set("homes." + args[0], null);
            } else {
                final Player player = Bukkit.getPlayer(args[1]);
                final User user = userManager.getUser(player);
                if (user.getHome(args[0]) == null) {
                    sender.sendMessage(fileManager.getMsg("delhome.errorplayer"));
                    return true;
                }
                user.removeHome(args[0]);
                playerFile.set("homes." + args[0], null);
                player.sendMessage(fileManager.getMsg("delhome.delhomeplayersender").replace("{PLAYER}", sender.getName()).replace("{HOME}", args[0]));
            }
            try {
                playerFile.save(new File(fileManager.getUsers(), args[1] + ".yml"));
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
            sender.sendMessage(fileManager.getMsg("delhome.player").replace("{PLAYER}", args[1]).replace("{HOME}", args[0]));
        } else {
            sender.sendMessage(fileManager.getMsg("delhome.usage"));
        }
        return true;
    }
}