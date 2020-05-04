package pl.crystalek.crctools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.model.MsgManager;
import pl.crystalek.crctools.model.User;
import pl.crystalek.crctools.model.UserManager;

public class TrybmsgCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final UserManager userManager;

    public TrybmsgCommand(final FileManager fileManager, final UserManager userManager) {
        this.fileManager = fileManager;
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("trybmsg")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return false;
            }
            Player player = (Player) sender;
            if (args.length != 0) {
                player.sendMessage(fileManager.getMsg("trybmsg.usage"));
                return false;
            }
            final User user = userManager.getUser(player);
            if (user.isMsg()) {
                user.setMsg(false);
                player.sendMessage(fileManager.getMsg("trybmsg.msgoff"));
            } else {
                user.setMsg(true);
                player.sendMessage(fileManager.getMsg("trybmsg.msgon"));
            }
        }
        return true;
    }
}
