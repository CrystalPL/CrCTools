package pl.crystalek.crctools.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;

public class TeleportUtil {

    public static void teleportTimer(final Player player, final Location locationToTp, final FileManager fileManager, final CrCTools crCTools) {
        new BukkitRunnable() {
            private final Location location = player.getLocation();
            private int i;

            public void run() {
                if (!player.isOnline()) {
                    cancel();
                }
                final Location newLocation = player.getLocation();
                if (location.getX() == newLocation.getX() && location.getY() == newLocation.getY() && location.getZ() == newLocation.getZ()) {
                    player.sendMessage(fileManager.getMsg("teleport.counter").replace("{TIME}", String.valueOf(fileManager.getInt("teleporttime") - i)));
                    i++;
                } else {
                    player.sendMessage(fileManager.getMsg("teleport.error"));
                    cancel();
                }

                if (i == fileManager.getInt("teleporttime")) {
                    cancel();
                    player.teleport(locationToTp);
                    player.sendMessage(fileManager.getMsg("teleport.success"));
                }
            }
        }.runTaskTimer(crCTools, 0L, 20L);
    }
}