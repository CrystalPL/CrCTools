package pl.crystalek.crctools.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.UserManager;

import java.io.IOException;

public class PlayerJoinListener implements Listener {
    private final FileManager fileManager;
    private final UserManager userManager;

    public PlayerJoinListener(final FileManager fileManager, final UserManager userManager) {
        this.fileManager = fileManager;
        this.userManager = userManager;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        fileManager.addConfiguration(player);
        if (player.hasPlayedBefore()) {
            fileManager.loadPlayer(player);
        } else {
            userManager.addUser(player);
            fileManager.savePlayer(player);
        }
    }
}
