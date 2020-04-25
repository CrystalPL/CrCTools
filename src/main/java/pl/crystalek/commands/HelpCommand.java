package pl.crystalek.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.managers.FileManager;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("feed")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    if (sender.hasPermission(FileManager.getPermission("feed.feed"))) {
                        Player player = (Player) sender;
                        if (player.getFoodLevel() != 20) {
                            player.setFoodLevel(20);
                            sender.sendMessage(FileManager.getMsg("feed.feed"));
                        } else {
                            sender.sendMessage(FileManager.getMsg("feed.error"));
                        }
                    } else {
                        sender.sendMessage(FileManager.getMsgPermission("feed.feed"));
                    }
                } else {
                    sender.sendMessage(FileManager.getMsg("notconsole"));
                }
            } else if (args.length == 1) {
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player player = Bukkit.getPlayer(args[0]);
                    if (sender.hasPermission(FileManager.getPermission("feed.player"))) {
                        if (player.getFoodLevel() != 20) {
                            player.setFoodLevel(20);
                            sender.sendMessage(FileManager.getMsg("feed.player").replace("{PLAYER}", player.getName()));
                            player.sendMessage(FileManager.getMsg("feed.feed"));
                        } else {
                            sender.sendMessage(FileManager.getMsg("feed.errorplayer").replace("{PLAYER}", player.getName()));
                        }
                    } else {
                        sender.sendMessage(FileManager.getMsgPermission("feed.player"));
                    }
                } else {
                    sender.sendMessage(FileManager.getMsg("offlineplayer"));
                }
            } else {
                sender.sendMessage(FileManager.getMsg("feed.usage"));
            }
        }
        return true;
    }
}
