package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.ChatUtil;

public final class NickCommand implements CommandExecutor {
    private final FileManager fileManager;

    public NickCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 1) {
            if (!sender.hasPermission(fileManager.getPermission("nick.nick"))) {
                sender.sendMessage(fileManager.getMsgPermission("nick.nick"));
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return true;
            }
            final Player player = (Player) sender;
            player.setDisplayName(ChatUtil.fixColor(args[0]));
            player.sendMessage(fileManager.getMsg("nick.nick").replace("{PLAYER}", player.getName()).replace("{NICK}", args[0]));
        } else if (args.length == 2) {
            if (!sender.hasPermission(fileManager.getPermission("nick.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("nick.player"));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) != null) {
                final Player player = Bukkit.getPlayer(args[0]);
                player.setDisplayName(ChatUtil.fixColor(args[1]));
                player.sendMessage(fileManager.getMsg("nick.nick"));
                sender.sendMessage(fileManager.getMsg("nick.player").replace("{PLAYER}", player.getName()).replace("{NICK}", args[1]));
            }
        } else {
            sender.sendMessage(fileManager.getMsg("nick.usage"));
        }
        return true;
    }
}
