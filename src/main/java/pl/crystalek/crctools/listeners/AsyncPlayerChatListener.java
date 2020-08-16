package pl.crystalek.crctools.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.crystalek.crctools.commands.ChatCommand;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.PermissionManager;
import pl.crystalek.crctools.managers.UserManager;
import pl.crystalek.crctools.model.Group;
import pl.crystalek.crctools.model.User;
import pl.crystalek.crctools.utils.ChatUtil;

import java.util.Comparator;
import java.util.List;

public final class AsyncPlayerChatListener implements Listener {
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
        if (!ChatCommand.CHAT) {
            if (!player.hasPermission(fileManager.getPermission("chat.bypass"))) {
                event.setCancelled(true);
                player.sendMessage(fileManager.getMsg("chat.error"));
            }
        } else {
            final User user = userManager.getUser(player);
            final List<String> permissionGroups = user.getPermissionGroups();
            permissionGroups.sort(Comparator.comparing(permissionManager::getGroup));
            final Group group = permissionManager.getGroup(permissionGroups.get(0));
            final String format = group.getFormat()
                    .replace("{PREFIX}", group.getPrefix())
                    .replace("{NICK}", "%1$s")
                    .replace("{MESSAGE}", "%2$s")
                    .replace("{LVL}", String.valueOf(player.getLevel()))
                    .replace("{FOODLVL}", String.valueOf(player.getFoodLevel()));
            event.setFormat(ChatUtil.fixColor(format));
            event.setMessage(ChatUtil.fixColor(user.getMessageColor() + event.getMessage()));
        }
    }
}
