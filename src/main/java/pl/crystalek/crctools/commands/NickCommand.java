package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.UserManager;
import pl.crystalek.crctools.model.User;
import pl.crystalek.crctools.utils.ChatUtil;

import java.io.File;
import java.io.IOException;

public final class NickCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final UserManager userManager;

    public NickCommand(final FileManager fileManager, final UserManager userManager) {
        this.fileManager = fileManager;
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 1) {
            if (!sender.hasPermission(fileManager.getPermission("nick.nick"))) {
                sender.sendMessage(fileManager.getMsgPermission("nick.nick"));
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return true;
            }
            final Player player = (Player) sender;
            final User user = userManager.getUser(player);
            final String nick = ChatColor.stripColor(args[0]);
            final YamlConfiguration playerFile = fileManager.getPlayerFile(args[0]);
            playerFile.set("nick", nick);
            player.setDisplayName(user.getNickColor() + nick);
            user.setDisplayName(nick);
            player.sendMessage(fileManager.getMsg("nick.nick").replace("{PLAYER}", player.getName()).replace("{NICK}", nick));
            try {
                playerFile.save(new File(fileManager.getUsers(), player.getName() + ".yml"));
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        } else if (args.length == 2) {
            if (!sender.hasPermission(fileManager.getPermission("nick.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("nick.player"));
                return true;
            }
            try {
                fileManager.getPlayerFile(args[0]);
            } catch (final NullPointerException exception) {
                sender.sendMessage(fileManager.getMsg("cantexist"));
                return true;
            }
            final YamlConfiguration playerFile = fileManager.getPlayerFile(args[0]);
            playerFile.set("nick", args[1]);
            if (Bukkit.getPlayer(args[0]) != null) {
                final Player player = Bukkit.getPlayer(args[0]);
                final String color = ChatUtil.fixColor(args[1]);
                player.setDisplayName(color);
                player.sendMessage(fileManager.getMsg("nick.nick").replace("{NICK}", color));
                sender.sendMessage(fileManager.getMsg("nick.player").replace("{PLAYER}", player.getName()).replace("{NICK}", color));
            }
            try {
                playerFile.save(new File(fileManager.getUsers(), args[0] + ".yml"));
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        } else {
            sender.sendMessage(fileManager.getMsg("nick.usage"));
        }
        return true;
    }
}
