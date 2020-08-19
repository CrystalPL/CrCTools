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
                setColors(player, args[1], "nick", (user, colorArg) -> {
                    user.setNickColor(colorArg);
                    player.setDisplayName(colorArg + user.getDisplayName());
                });
            } else if (args[0].equalsIgnoreCase("message")) {
                setColors(player, args[1], "message", User::setMessageColor);
            } else {
                printHelp(sender);
                return true;
            }

        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("nick")) {
                setColors(sender, args, "nick", (user, colorArg) -> {
                    user.setNickColor(colorArg);
                    Bukkit.getPlayer(args[1]).setDisplayName(colorArg + user.getDisplayName());
                });
            } else if (args[0].equalsIgnoreCase("message")) {
                setColors(sender, args, "message", User::setMessageColor);
            } else {
                printHelp(sender);
            }
        } else {
            printHelp(sender);
        }
        return true;
    }

    private void setColors(final Player player, final String color, final String colorType, final BiConsumer<User, String> consumer) {
        if (!player.hasPermission(fileManager.getPermission("color." + colorType))) {
            player.sendMessage(fileManager.getMsgPermission("color." + colorType));
            return;
        }
        final YamlConfiguration playerFile = fileManager.getPlayerFile(player.getName());
        String colorArg = ChatUtil.fixColor(color);
        if (colorArg.equalsIgnoreCase("clear")) {
            colorArg = "";
            player.sendMessage(fileManager.getMsg("color." + colorType + "clear"));
        } else {
            if (!checkColor(colorArg)) {
                player.sendMessage(fileManager.getMsg("color.error"));
                return;
            } else {
                player.sendMessage(fileManager.getMsg("color." + colorType + "change").replace("{COLOR}", colorArg).replace("{PLAYER}", ChatColor.stripColor(player.getDisplayName())));
            }
        }
        final User user = userManager.getUser(player);
        consumer.accept(user, colorArg);
        if (colorType.equalsIgnoreCase("nick")) {
            user.setNickColor(colorArg);
            player.setDisplayName(colorArg + user.getDisplayName());
        } else {
            user.setMessageColor(colorArg);
        }
        playerFile.set(colorType + "color", colorArg);
        try {
            playerFile.save(new File(fileManager.getUsers(), player.getName() + ".yml"));
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    private void setColors(final CommandSender sender, final String[] args, final String colorType, final BiConsumer<User, String> consumer) {
        if (!sender.hasPermission(fileManager.getPermission("color." + colorType + "player"))) {
            sender.sendMessage(fileManager.getMsgPermission("color." + colorType + "player"));
            return;
        }
        try {
            fileManager.getPlayerFile(args[1]);
        } catch (final NullPointerException exception) {
            sender.sendMessage(fileManager.getMsg("cantexist"));
            return;
        }
        final YamlConfiguration playerFile = fileManager.getPlayerFile(args[1]);
        String colorArg = ChatUtil.fixColor(args[2]);
        if (colorArg.equalsIgnoreCase("clear")) {
            colorArg = "";
        } else {
            if (!checkColor(colorArg)) {
                sender.sendMessage(fileManager.getMsg("color.error"));
                return;
            }
        }
        if (Bukkit.getPlayer(args[1]) != null) {
            final Player player = Bukkit.getPlayer(args[1]);
            final User user = userManager.getUser(player);
            consumer.accept(user, colorArg);
            if (colorArg.equalsIgnoreCase("clear")) {
                player.sendMessage(fileManager.getMsg("color." + colorType + "clear"));
                sender.sendMessage(fileManager.getMsg("color." + colorType + "clearplayer").replace("{PLAYER}", player.getName()));
            } else {
                player.sendMessage(fileManager.getMsg("color." + colorType + "change").replace("{COLOR}", colorArg).replace("{PLAYER}", ChatColor.stripColor(player.getDisplayName())));
                sender.sendMessage(fileManager.getMsg("color." + colorType + "changeplayer").replace("{COLOR}", colorArg).replace("{PLAYER}", ChatColor.stripColor(player.getDisplayName())));
            }
        }
        playerFile.set(colorType + "color", colorArg);
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
