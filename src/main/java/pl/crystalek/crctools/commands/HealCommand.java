package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public class HealCommand implements CommandExecutor {
    FileManager fileManager = new FileManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("heal")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    if (sender.hasPermission(fileManager.getPermission("heal.heal"))) {
                        Player player = (Player) sender;
                        if (player.getHealth() != 20) {
                            player.setHealth(20);
                            sender.sendMessage(fileManager.getMsg("heal.heal"));
                        } else {
                            sender.sendMessage(fileManager.getMsg("heal.error"));
                        }
                    } else {
                        sender.sendMessage(fileManager.getMsgPermission("heal.heal"));
                    }
                } else {
                    sender.sendMessage(fileManager.getMsg("notconsole"));
                }
            } else if (args.length == 1) {
                if (sender.hasPermission(fileManager.getPermission("heal.player"))) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        Player player = Bukkit.getPlayer(args[0]);
                        if (player.getHealth() != 20) {
                            player.setHealth(20);
                            sender.sendMessage(fileManager.getMsg("heal.player").replace("{PLAYER}", player.getName()));
                            player.sendMessage(fileManager.getMsg("heal.heal"));
                        } else {
                            sender.sendMessage(fileManager.getMsg("heal.errorplayer").replace("{PLAYER}", player.getName()));
                        }
                    } else {
                        sender.sendMessage(fileManager.getMsg("offlineplayer"));
                    }
                } else {
                    sender.sendMessage(fileManager.getMsgPermission("heal.player"));
                }
            } else {
                sender.sendMessage(fileManager.getMsg("heal.usage"));
            }
        }
        return true;
    }
}
