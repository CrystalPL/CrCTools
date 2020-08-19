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

public class KickCommand implements CommandExecutor {
    private final FileManager fileManager;

    public KickCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("kick.player"))) {
            sender.sendMessage(fileManager.getMsgPermission("kick.player"));
            return true;
        }
        final String kickReason;
        if (args.length == 0) {
            sender.sendMessage(fileManager.getMsg("kick.usage"));
            return true;
        } else if (args.length == 1) {
            kickReason = fileManager.getMsg("kick.defaultreason");
        } else {
            kickReason = ChatUtil.fixColor(StringUtils.join(args, ' ', 1, args.length));
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(fileManager.getMsg("offlineplayer"));
            return true;
        }
        final Player player = Bukkit.getPlayer(args[0]);
        if (player.hasPermission(fileManager.getPermission("kick.bypass"))) {
            sender.sendMessage(fileManager.getMsg("kick.bypass").replace("{PERMISSION}", fileManager.getPermission("kick.bypass")));
            return true;
        }
        final List<String> msgList = fileManager.getMsgList("kick.kick");
        final String kick = String.join("\n", msgList).replace("{PLAYER}", player.getName()).replace("{REASON}", kickReason).replace("{ADMIN}", sender.getName()).replace("{DATE}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        player.kickPlayer(kick);
        sender.sendMessage(fileManager.getMsg("kick.sender").replace("{PLAYER}", player.getName()));
        if (fileManager.getBoolean("kickbroadcast")) {
            Bukkit.broadcastMessage(fileManager.getMsg("kick.kickbroadcast").replace("{PLAYER}", player.getName()).replace("{REASON}", kickReason).replace("{ADMIN}", sender.getName()));
        }
        return true;
    }
}
