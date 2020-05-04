package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public class FlyCommand implements CommandExecutor {
    private final FileManager fileManager;

    public FlyCommand(final FileManager fileManager) {
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
            if (!player.hasPermission(fileManager.getPermission("fly.fly"))) {
                player.sendMessage(fileManager.getMsgPermission("fly.fly"));
                return true;
            }
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.sendMessage(fileManager.getMsg("fly.takeoff"));
            } else {
                player.setAllowFlight(true);
                player.sendMessage(fileManager.getMsg("fly.takeon"));
            }
        } else if (args.length == 1) {
            if (!sender.hasPermission(fileManager.getPermission("fly.fly"))) {
                sender.sendMessage(fileManager.getMsgPermission("fly.fly"));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(fileManager.getMsg("offlineplayer"));
                return true;
            }
            final Player player = (Bukkit.getPlayer(args[0]));
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.sendMessage(fileManager.getMsg("fly.takeoff"));
                sender.sendMessage(fileManager.getMsg("fly.takeoffplayer").replace("{PLAYER}", player.getName()));
            } else {
                player.setAllowFlight(true);
                player.sendMessage(fileManager.getMsg("fly.takeon"));
                sender.sendMessage(fileManager.getMsg("fly.takeonplayer").replace("{PLAYER}", player.getName()));
            }
        } else {
            sender.sendMessage(fileManager.getMsg("fly.usage"));
        }
        return true;
    }
}