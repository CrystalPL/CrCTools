package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public class PingCommand implements CommandExecutor {
    private final FileManager fileManager;

    public PingCommand(final FileManager fileManager) {
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
            sender.sendMessage(fileManager.getMsg("ping.ping").replace("{PING}", String.valueOf(player.spigot().getPing())));
        } else if (args.length == 1) {
            if (!sender.hasPermission(fileManager.getPermission("ping.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("ping.player"));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(fileManager.getMsg("offlineplayer"));
                return true;
            }
            final Player player = Bukkit.getPlayer(args[0]);
            sender.sendMessage(fileManager.getMsg("ping.player").replace("{PING}", String.valueOf(player.spigot().getPing())).replace("{PLAYER}", player.getName()));
        } else {
            sender.sendMessage(fileManager.getMsg("ping.usage"));
        }
        return true;
    }
}
