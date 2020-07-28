package pl.crystalek.crctools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.MsgManager;

public final class SocialspyCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final MsgManager msgManager;

    public SocialspyCommand(final FileManager fileManager, final MsgManager msgManager) {
        this.fileManager = fileManager;
        this.msgManager = msgManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        final Player player = (Player) sender;
        if (!sender.hasPermission(fileManager.getPermission("socialspy.socialspy"))) {
            sender.sendMessage(fileManager.getMsgPermission("socialspy.socialspy"));
            return true;
        }
        if (msgManager.addSpy(player)) {
            player.sendMessage(fileManager.getMsg("socialspy.socialspyon"));
        } else {
            player.sendMessage(fileManager.getMsg("socialspy.socialspyoff"));
        }
        return true;
    }
}
