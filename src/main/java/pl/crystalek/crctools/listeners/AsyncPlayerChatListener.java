package pl.crystalek.crctools.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.PermissionManager;
import pl.crystalek.crctools.managers.UserManager;
import pl.crystalek.crctools.model.User;
import pl.crystalek.crctools.utils.ChatUtil;

import java.util.Comparator;
import java.util.List;

public class AsyncPlayerChatListener implements Listener {
    private final FileManager fileManager;
    private final PermissionManager permissionManager;
    private final UserManager userManager;

    public AsyncPlayerChatListener(final FileManager fileManager, final PermissionManager permissionManager, final UserManager userManager) {
        this.fileManager = fileManager;
        this.permissionManager = permissionManager;
        this.userManager = userManager;
    }

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        if (!CrCTools.CHAT) {
            if (!player.hasPermission(fileManager.getPermission("chat.bypass"))) {
                event.setCancelled(true);
                player.sendMessage(fileManager.getMsg("chat.error"));
            }
        } else {
            final User user = userManager.getUser(player);
            final List<String> permissionGroups = user.getPermissionGroups();
            System.out.println(permissionGroups);
            permissionGroups.sort(Comparator.comparing(permissionManager::getGroup));
            System.out.println(permissionGroups);
            final String format = "{PREFIX} {NICK} &8» &7{MESSAGE}"
                    .replace("{PREFIX}", permissionManager.getGroup(permissionGroups.get(0)).getPrefix())
                    .replace("{NICK}", "%1$s")
                    .replace("{MESSAGE}", "%2$s");
            event.setFormat(ChatUtil.fixColor(format));
            event.setMessage(ChatUtil.fixColor(event.getMessage()));
        }
    }
}
