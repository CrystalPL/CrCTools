package pl.crystalek.crctools.commands;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
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
import java.util.List;
import java.util.function.BiConsumer;

public final class ColorCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final UserManager userManager;

    public ColorCommand(final FileManager fileManager, final UserManager userManager) {
        this.fileManager = fileManager;
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 2) {
            if (!sender.hasPermission(fileManager.getPermission("color.color"))) {
                sender.sendMessage(fileManager.getMsgPermission("color.color"));
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return true;
            }
            final Player player = (Player) sender;
            if (args[0].equalsIgnoreCase("nick")) {
                setColors(player, args, "nickcolor", "color.nick", (BiConsumer<User, String>) (user, colorArg) -> {
                    user.setNickColor(colorArg);
                    player.setDisplayName(ChatUtil.fixColor(colorArg) + user.getDisplayName());
                });
                player.sendMessage(fileManager.getMsg("color.nickchange").replace("{COLOR}", ChatUtil.fixColor(args[1]) + player.getName()));
            } else if (args[0].equalsIgnoreCase("message")) {
                setColors(player, args, "messagecolor", "color.message", User::setMessageColor);
                player.sendMessage(fileManager.getMsg("color.messagechange").replace("{COLOR}", args[1]));
            } else {
                printHelp(sender);
                return true;
            }

        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("nick")) {
                setColors(sender, args, "nickcolor", "color.nickplayer", (user, player) -> {
                    user.setNickColor(args[2]);
                    player.setDisplayName(ChatUtil.fixColor(args[2]) + user.getDisplayName());
                });
                final Player player = Bukkit.getPlayer(args[1]);
                player.sendMessage(fileManager.getMsg("color.nickchange").replace("{COLOR}", ChatUtil.fixColor(args[2]) + player.getName()));
                sender.sendMessage(fileManager.getMsg("color.nickchangeplayer").replace("{COLOR}", ChatUtil.fixColor(args[2]) + player.getName()).replace("{PLAYER}", player.getName()));
            } else if (args[0].equalsIgnoreCase("message")) {
                setColors(sender, args, "messagecolor", "color.messageplayer", (user, player) -> user.setMessageColor(args[2]));
                final Player player = Bukkit.getPlayer(args[1]);
                player.sendMessage(fileManager.getMsg("color.messagechange").replace("{COLOR}", ChatUtil.fixColor(args[2])));
                sender.sendMessage(fileManager.getMsg("color.messagechangeplayer").replace("{COLOR}", ChatUtil.fixColor(args[2])).replace("{PLAYER}", player.getName()));
            } else {
                printHelp(sender);
            }
        } else {
            printHelp(sender);
        }
        return true;
    }

    private void setColors(final Player player, final String[] args, final String colorType, final String permission, final BiConsumer<User, String> consumer) {
        if (!player.hasPermission(fileManager.getPermission(permission))) {
            player.sendMessage(fileManager.getMsgPermission(permission));
            return;
        }
        final YamlConfiguration playerFile = fileManager.getPlayerFile(player.getName());
        final String colorArg = args[1];
        if (!checkColor(colorArg)) {
            player.sendMessage(fileManager.getMsg("color.error"));
            return;
        }
        final User user = userManager.getUser(player);
        consumer.accept(user, colorArg);
        playerFile.set(colorType, colorArg);
        try {
            playerFile.save(new File(fileManager.getUsers(), player.getName() + ".yml"));
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    private void setColors(final CommandSender sender, final String[] args, final String colorType, final String permission, final BiConsumer<User, Player> consumer) {
        if (!sender.hasPermission(fileManager.getPermission(permission))) {
            sender.sendMessage(fileManager.getMsgPermission(permission));
            return;
        }
        try {
            fileManager.getPlayerFile(args[1]);
        } catch (final NullPointerException exception) {
            sender.sendMessage(fileManager.getMsg("cantexist"));
            return;
        }
        final YamlConfiguration playerFile = fileManager.getPlayerFile(args[1]);
        final String colorArg = args[2];
        if (!checkColor(colorArg)) {
            sender.sendMessage(fileManager.getMsg("color.error"));
            return;
        }
        if (Bukkit.getPlayer(args[1]) != null) {
            final Player player = Bukkit.getPlayer(args[1]);
            final User user = userManager.getUser(player);
            consumer.accept(user, player);
        }
        playerFile.set(colorType, colorArg);
        try {
            playerFile.save(new File(fileManager.getUsers(), args[1] + ".yml"));
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    private boolean checkColor(final String colorArg) {
        return StringUtils.isBlank(ChatColor.stripColor(ChatUtil.fixColor(colorArg)));
    }

    private void printHelp(final CommandSender sender) {
        final List<String> msgList = fileManager.getMsgList("color.usage");
        for (final String message : msgList) {
            sender.sendMessage(message);
        }
    }
}
