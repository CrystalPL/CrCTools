package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public class TpAllCommand implements CommandExecutor {
    private final FileManager fileManager;

    public TpAllCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("tpall.tpall"))) {
            sender.sendMessage(fileManager.getMsgPermission("tpall.tpall"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission(fileManager.getPermission("tpall.bypass"))) {
                player.teleport((Player) sender);
                player.sendMessage(fileManager.getMsg("tp.player").replace("{PLAYER}", sender.getName()));
            }
        }
        return false;
    }
}
