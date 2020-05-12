package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public class KickallCommand implements CommandExecutor {
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
        String msg = fileManager.getMsg("kick.kick");
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission(fileManager.getPermission("kick.bypass"))) {
                player.kickPlayer(fileManager.getMsg("kick.kick"));
            }
        }
        sender.sendMessage(fileManager.getMsg("kick.all"));
        return true;
    }
}
