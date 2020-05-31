package pl.crystalek.crctools.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;

public class AsyncPlayerChatListener implements Listener {
    private final FileManager fileManager;

    public AsyncPlayerChatListener(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        if (!CrCTools.CHAT) {
            final Player player = event.getPlayer();
            if (!player.hasPermission(fileManager.getPermission("chat.bypass"))) {
                event.setCancelled(true);
                player.sendMessage(fileManager.getMsg("chat.error"));
            }
        }
    }
}
