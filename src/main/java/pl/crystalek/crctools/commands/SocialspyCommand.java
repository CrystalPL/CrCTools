package pl.crystalek.crctools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.model.MsgManager;

public class SocialspyCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final MsgManager msgManager;

    public SocialspyCommand(final FileManager fileManager, final MsgManager msgManager) {
        this.fileManager = fileManager;
        this.msgManager = msgManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("socialspy")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return false;
            }
            if (!sender.hasPermission(fileManager.getPermission("socialspy.socialspy"))) {
                sender.sendMessage(fileManager.getMsgPermission("socialspy.socialspy"));
                return false;
            }
            Player player = (Player) sender;
            if (msgManager.addSpy(player)) {
                player.sendMessage(fileManager.getMsg("socialspy.socialspyon"));
            } else {
                player.sendMessage(fileManager.getMsg("socialspy.socialspyoff"));
            }
        }
        return true;
    }
}
