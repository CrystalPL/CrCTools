package pl.crystalek.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.managers.FileManager;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("heal")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    if (sender.hasPermission(FileManager.getPermission("heal.heal"))) {
                        Player player = (Player) sender;
                        if (player.getHealth() != 20) {
                            player.setHealth(20);
                            sender.sendMessage(FileManager.getMsg("heal.heal"));
                        } else {
                            sender.sendMessage(FileManager.getMsg("heal.error"));
                        }
                    } else {
                        sender.sendMessage(FileManager.getMsgPermission("heal.heal"));
                    }
                } else {
                    sender.sendMessage(FileManager.getMsg("notconsole"));
                }
            } else if (args.length == 1) {
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player player = Bukkit.getPlayer(args[0]);
                    if (sender.hasPermission(FileManager.getPermission("heal.player"))) {
                        if (player.getHealth() != 20) {
                            player.setHealth(20);
                            sender.sendMessage(FileManager.getMsg("heal.player").replace("{PLAYER}", player.getName()));
                            player.sendMessage(FileManager.getMsg("heal.heal"));
                        } else {
                            sender.sendMessage(FileManager.getMsg("heal.errorplayer").replace("{PLAYER}", player.getName()));
                        }
                    } else {
                        sender.sendMessage(FileManager.getMsgPermission("heal.player"));
                    }
                } else {
                    sender.sendMessage(FileManager.getMsg("offlineplayer"));
                }
            } else {
                sender.sendMessage(FileManager.getMsg("heal.usage"));
            }
        }
        return true;
    }
}
