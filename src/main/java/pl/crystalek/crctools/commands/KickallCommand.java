package pl.crystalek.crctools.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.ChatUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class KickallCommand implements CommandExecutor {
    private final FileManager fileManager;

    public KickallCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("kick.all"))) {
            sender.sendMessage(fileManager.getMsgPermission("kick.all"));
            return true;
        }
        final String kickReason;
        if (args.length == 0) {
            kickReason = fileManager.getMsg("kick.defaultreason");
        } else {
            kickReason = ChatUtil.fixColor(StringUtils.join(args, ' ', 0, args.length));
        }
        final List<String> msgList = fileManager.getMsgList("kick.kick");
        final String kick = String.join("\n", msgList).replace("{REASON}", kickReason).replace("{ADMIN}", sender.getName()).replace("{DATE}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        final String permission = fileManager.getPermission("kick.bypass");
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission(permission)) {
                player.kickPlayer(kick.replace("{PLAYER}", player.getName()));
            }
        }
        sender.sendMessage(fileManager.getMsg("kick.all"));
        return true;
    }
}
