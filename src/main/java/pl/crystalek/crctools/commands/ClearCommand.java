package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public class ClearCommand implements CommandExecutor {
    private final FileManager fileManager;

    public ClearCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return true;
            }
            final Player player = (Player) sender;
            if (!player.hasPermission(fileManager.getPermission("clear.clear"))) {
                player.sendMessage(fileManager.getMsgPermission("clear.clear"));
                return true;
            }
            player.getInventory().clear();
            player.sendMessage(fileManager.getMsg("clear.clear"));
        } else if (args.length == 1) {
            if (!sender.hasPermission(fileManager.getPermission("clear.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("clear.player"));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(fileManager.getMsg("offlineplayer"));
                return true;
            }
            final Player player = Bukkit.getPlayer(args[0]);
            player.getInventory().clear();
            player.sendMessage(fileManager.getMsg("clear.clear"));
            sender.sendMessage(fileManager.getMsg("clear.player").replace("{PLAYER}", player.getName()));

        }
        return true;
    }
}
