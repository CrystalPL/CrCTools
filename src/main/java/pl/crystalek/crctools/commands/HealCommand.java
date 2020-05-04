package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public class HealCommand implements CommandExecutor {
    private final FileManager fileManager;

    public HealCommand(final FileManager fileManager) {
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
            if (!player.hasPermission(fileManager.getPermission("heal.heal"))) {
                player.sendMessage(fileManager.getMsgPermission("heal.heal"));
                return true;
            }
            if (player.getHealth() != 20) {
                player.setHealth(20);
                sender.sendMessage(fileManager.getMsg("heal.heal"));
            } else {
                sender.sendMessage(fileManager.getMsg("heal.error"));
            }
        } else if (args.length == 1) {
            if (!sender.hasPermission(fileManager.getPermission("heal.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("heal.player"));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(fileManager.getMsg("offlineplayer"));
                return true;
            }
            final Player player = Bukkit.getPlayer(args[0]);
            if (player.getHealth() != 20) {
                player.setHealth(20);
                sender.sendMessage(fileManager.getMsg("heal.player").replace("{PLAYER}", player.getName()));
                player.sendMessage(fileManager.getMsg("heal.heal"));
            } else {
                sender.sendMessage(fileManager.getMsg("heal.errorplayer").replace("{PLAYER}", player.getName()));
            }
        } else {
            sender.sendMessage(fileManager.getMsg("heal.usage"));
        }
        return true;
    }
}