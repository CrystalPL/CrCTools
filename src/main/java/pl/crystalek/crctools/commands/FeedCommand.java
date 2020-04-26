package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public class FeedCommand implements CommandExecutor {
    FileManager fileManager = new FileManager();
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("feed")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    if (sender.hasPermission(fileManager.getPermission("feed.feed"))) {
                        Player player = (Player) sender;
                        if (player.getFoodLevel() != 20) {
                            player.setFoodLevel(20);
                            sender.sendMessage(fileManager.getMsg("feed.feed"));
                        } else {
                            sender.sendMessage(fileManager.getMsg("feed.error"));
                        }
                    } else {
                        sender.sendMessage(fileManager.getMsgPermission("feed.feed"));
                    }
                } else {
                    sender.sendMessage(fileManager.getMsg("notconsole"));
                }
            } else if (args.length == 1) {
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player player = Bukkit.getPlayer(args[0]);
                    if (sender.hasPermission(fileManager.getPermission("feed.player"))) {
                        if (player.getFoodLevel() != 20) {
                            player.setFoodLevel(20);
                            sender.sendMessage(fileManager.getMsg("feed.player").replace("{PLAYER}", player.getName()));
                            player.sendMessage(fileManager.getMsg("feed.feed"));
                        } else {
                            sender.sendMessage(fileManager.getMsg("feed.errorplayer").replace("{PLAYER}", player.getName()));
                        }
                    } else {
                        sender.sendMessage(fileManager.getMsgPermission("feed.player"));
                    }
                } else {
                    sender.sendMessage(fileManager.getMsg("offlineplayer"));
                }
            } else {
                sender.sendMessage(fileManager.getMsg("feed.usage"));
            }
        }
        return true;
    }
}
