package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public final class KillCommand implements CommandExecutor {
    private final FileManager fileManager;

    public KillCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }


    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length != 1) {
            sender.sendMessage(fileManager.getMsg("kill.usage"));
            return true;
        }
        if (!sender.hasPermission(fileManager.getPermission("kill.kill"))) {
            sender.sendMessage(fileManager.getMsgPermission("kill.kill"));
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(fileManager.getMsg("offlineplayer"));
            return true;
        }
        final Player player = Bukkit.getPlayer(args[0]);
        player.setHealth(0);
        sender.sendMessage(fileManager.getMsg("kill.player").replace("{PLAYER}", player.getName()));
        player.sendMessage(fileManager.getMsg("kill.kill").replace("{PLAYER}", sender.getName()));
        return true;
    }
}
