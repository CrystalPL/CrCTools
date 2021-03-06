package pl.crystalek.crctools.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.crystalek.crctools.managers.FileManager;

public final class PlayerCommandPreprocessListener implements Listener {
    private final FileManager fileManager;

    public PlayerCommandPreprocessListener(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final String message = event.getMessage();
        if (message.equalsIgnoreCase("/reload") || message.equalsIgnoreCase("/rl")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(fileManager.getMsg("reload.usage"));
        }
    }
}
