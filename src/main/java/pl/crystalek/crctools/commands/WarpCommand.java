package pl.crystalek.crctools.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.WarpManager;

public class WarpCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final WarpManager warpManager;
    private final CrCTools crCTools;

    public WarpCommand(final FileManager fileManager, final WarpManager warpManager, final CrCTools crCTools) {
        this.fileManager = fileManager;
        this.warpManager = warpManager;
        this.crCTools = crCTools;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (warpManager.printWarpList(sender, args, warpManager, fileManager)) return true;

        teleportTimer((Player) sender, warpManager.getWarp(args[0]).getLocation());
        return true;
    }

    private void teleportTimer(final Player player, final Location warp) {
        new BukkitRunnable() {
            private final Location location = player.getLocation();
            private int i;

            public void run() {
                if (!player.isOnline()) {
                    cancel();
                }
                final Location newLocation = player.getLocation();
                if (location.getX() == newLocation.getX() && location.getY() == newLocation.getY() && location.getZ() == newLocation.getZ()) {
                    player.sendMessage(fileManager.getMsg("warp.warp").replace("{TIME}", String.valueOf(fileManager.getInt("teleporttime") - i)));
                    i++;
                } else {
                    player.sendMessage(fileManager.getMsg("tpaccept.error"));
                    cancel();
                }

                if (i == fileManager.getInt("teleporttime")) {
                    player.teleport(warp);
                    player.sendMessage(fileManager.getMsg("tpaccept.success"));
                    cancel();
                }
            }
        }.runTaskTimer(crCTools, 0L, 20L);
    }
}