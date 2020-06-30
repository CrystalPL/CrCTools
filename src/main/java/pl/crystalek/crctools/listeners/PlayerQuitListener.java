package pl.crystalek.crctools.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.PermissionManager;
import pl.crystalek.crctools.managers.UserManager;

import java.io.IOException;

public class PlayerQuitListener implements Listener {
    private final FileManager fileManager;
    private final UserManager userManager;
    private final PermissionManager permissionManager;

    public PlayerQuitListener(final FileManager fileManager, final UserManager userManager, final PermissionManager permissionManager) {
        this.fileManager = fileManager;
        this.userManager = userManager;
        this.permissionManager = permissionManager;
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) throws IOException {
        final Player player = event.getPlayer();
        fileManager.savePlayer(player);
        userManager.removeUser(player);
        fileManager.removeConfiguration(player);
    }
}
