package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public final class FeedCommand implements CommandExecutor {
    private final FileManager fileManager;

    public FeedCommand(final FileManager fileManager) {
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
            if (!player.hasPermission(fileManager.getPermission("feed.feed"))) {
                player.sendMessage(fileManager.getMsgPermission("feed.feed"));
                return true;
            }
            if (player.getFoodLevel() != 20) {
                player.setFoodLevel(20);
                player.sendMessage(fileManager.getMsg("feed.feed"));
            } else {
                player.sendMessage(fileManager.getMsg("feed.error"));
            }
        } else if (args.length == 1) {
            if (!sender.hasPermission(fileManager.getPermission("feed.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("feed.player"));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(fileManager.getMsg("offlineplayer"));
                return true;
            }
            final Player player = Bukkit.getPlayer(args[0]);
            if (player.getFoodLevel() != 20) {
                player.setFoodLevel(20);
                sender.sendMessage(fileManager.getMsg("feed.player").replace("{PLAYER}", player.getName()));
                player.sendMessage(fileManager.getMsg("feed.feed"));
            } else {
                sender.sendMessage(fileManager.getMsg("feed.errorplayer").replace("{PLAYER}", player.getName()));
            }
        } else {
            sender.sendMessage(fileManager.getMsg("feed.usage"));
        }
        return true;
    }
}