package pl.crystalek.crctools.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.exceptions.GroupExistException;
import pl.crystalek.crctools.exceptions.GroupHasException;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.MailManager;
import pl.crystalek.crctools.managers.PermissionManager;
import pl.crystalek.crctools.managers.UserManager;
import pl.crystalek.crctools.utils.ChatUtil;

import java.io.IOException;

public final class PlayerJoinListener implements Listener {
    private final FileManager fileManager;
    private final UserManager userManager;
    private final PermissionManager permissionManager;
    private final MailManager mailManager;
    private final CrCTools crCTools;

    public PlayerJoinListener(final FileManager fileManager, final UserManager userManager, final PermissionManager permissionManager, final MailManager mailManager, final CrCTools crCTools) {
        this.fileManager = fileManager;
        this.userManager = userManager;
        this.permissionManager = permissionManager;
        this.mailManager = mailManager;
        this.crCTools = crCTools;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) throws IOException, GroupExistException, GroupHasException {
        final Player player = event.getPlayer();
        fileManager.addConfiguration(player);
        if (player.hasPlayedBefore()) {
            fileManager.loadPlayer(player);
        } else {
            userManager.addUser(player);
            permissionManager.addGroup(player.getName(), crCTools.getConfig().getString("defaultgroup"));
            player.setDisplayName(ChatUtil.fixColor(crCTools.getConfig().getString("defaultmessagecolor")) + player.getName());
            fileManager.savePlayer(player);
        }
        permissionManager.loadPermission(player);
        mailManager.loadMessage(player);
    }
}