package pl.crystalek.crctools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.UserManager;
import pl.crystalek.crctools.model.User;

public class GodCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final UserManager userManager;

    public GodCommand(final FileManager fileManager, final UserManager userManager) {
        this.fileManager = fileManager;
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        final Player player = (Player) sender;
        if (!player.hasPermission(fileManager.getPermission("god.god"))) {
            player.sendMessage(fileManager.getMsgPermission("god.god"));
            return true;
        }
        final User user = userManager.getUser(player);
        if (user.isGod()) {
            user.setGod(false);
            player.sendMessage(fileManager.getMsg("god.godoff"));
        } else {
            user.setGod(true);
            player.sendMessage(fileManager.getMsg("god.godon"));
        }
        return true;
    }
}
