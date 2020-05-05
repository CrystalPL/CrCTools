package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public class SCommand implements CommandExecutor {
    private final FileManager fileManager;

    public SCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length != 1) {
            sender.sendMessage(fileManager.getMsg("s.usage"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (!sender.hasPermission(fileManager.getPermission("s.s"))) {
            sender.sendMessage(fileManager.getMsgPermission("s.s"));
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(fileManager.getMsg("offlineplayer"));
            return true;
        }
        final Player player = Bukkit.getPlayer(args[0]);
        player.teleport((Player) sender);
        sender.sendMessage(fileManager.getMsg("s.success").replace("{PLAYER}", player.getName()));
        player.sendMessage(fileManager.getMsg("tp.player").replace("{PLAYER}", sender.getName()));
        return true;
    }
}
