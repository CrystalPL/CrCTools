package pl.crystalek.crctools.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.crystalek.crctools.managers.FileManager;

public class PlayerCommandPreprocessListener implements Listener {
    private final FileManager fileManager;

    public PlayerCommandPreprocessListener(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final String message = "/" + event.getMessage();
        if (message.startsWith("reload") || message.equalsIgnoreCase("rl")) {
            System.out.println("0YUVAISDYHADYVHIAYID");
            event.setCancelled(true);
            event.getPlayer().sendMessage(fileManager.getMsg("reload.usage"));
        }
    }
}
