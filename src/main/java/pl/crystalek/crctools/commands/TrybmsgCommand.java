package pl.crystalek.crctools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.UserManager;
import pl.crystalek.crctools.model.User;

public final class TrybmsgCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final UserManager userManager;

    public TrybmsgCommand(final FileManager fileManager, final UserManager userManager) {
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
        if (args.length != 0) {
            player.sendMessage(fileManager.getMsg("trybmsg.usage"));
            return true;
        }
        final User user = userManager.getUser(player);
        if (user.isMsg()) {
            user.setMsg(false);
            player.sendMessage(fileManager.getMsg("trybmsg.msgoff"));
        } else {
            user.setMsg(true);
            player.sendMessage(fileManager.getMsg("trybmsg.msgon"));
        }
        return true;
    }
}
