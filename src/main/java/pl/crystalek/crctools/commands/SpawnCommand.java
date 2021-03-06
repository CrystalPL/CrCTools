package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.ServerOptions;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.TeleportUtil;

public final class SpawnCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final CrCTools crCTools;

    public SpawnCommand(final FileManager fileManager, final CrCTools crCTools) {
        this.fileManager = fileManager;
        this.crCTools = crCTools;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        Player player = (Player) sender;
        final Location spawnLocation = ServerOptions.getSpawnLocation();
        if (spawnLocation == null) {
            sender.sendMessage(fileManager.getMsg("spawn.error"));
            return true;

        }
        if (args.length == 1) {
            if (!player.hasPermission(fileManager.getPermission("spawn.player"))) {
                player.sendMessage(fileManager.getMsgPermission("spawn.player"));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                player.sendMessage(fileManager.getMsg("offlineplayer"));
                return true;
            }
            player = Bukkit.getPlayer(args[0]);
        }
        TeleportUtil.teleportTimer(player, spawnLocation, fileManager, crCTools);
        return true;
    }
}