package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public class TpCommand implements CommandExecutor {
    private final FileManager fileManager;

    public TpCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(args.length >= 1 && args.length <= 2)) {
            sender.sendMessage(fileManager.getMsg("tp.usage"));
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(fileManager.getMsg("offlineplayer"));
            return true;
        }
        final Player player1 = Bukkit.getPlayer(args[0]);
        if (args.length == 2) {
            if (!sender.hasPermission(fileManager.getPermission("tp.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("tp.player"));
                return true;
            }
            if (Bukkit.getPlayer(args[1]) == null) {
                sender.sendMessage(fileManager.getMsg("offlineplayer"));
                return true;
            }
            final Player player2 = Bukkit.getPlayer(args[1]);
            if (player1.equals(sender) || player2.equals(sender)) {
                sender.sendMessage(fileManager.getMsg("tp.usage"));
                return true;
            }
            player1.teleport(player2);
            sender.sendMessage(fileManager.getMsg("tp.sender").replace("{PLAYER1}", player1.getName()).replace("{PLAYER2}", player2.getName()));
            player1.sendMessage(fileManager.getMsg("tp.player").replace("{PLAYER}", player2.getName()));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (!sender.hasPermission(fileManager.getPermission("tp.player"))) {
            sender.sendMessage(fileManager.getMsgPermission("tp.player"));
            return true;
        }
        ((Player) sender).teleport(player1);
        sender.sendMessage(fileManager.getMsg("tp.player").replace("{PLAYER}", player1.getName()));
        return true;
    }
}
