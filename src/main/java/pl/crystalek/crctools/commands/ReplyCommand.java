package pl.crystalek.crctools.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.MsgManager;

import java.util.List;

public final class ReplyCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final MsgManager msgManager;

    public ReplyCommand(final FileManager fileManager, final MsgManager msgManager) {
        this.fileManager = fileManager;
        this.msgManager = msgManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("r")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return true;
            }
            final Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(fileManager.getMsg("reply.usage"));
                return true;
            }
            final Player target = msgManager.getMsg(player);
            if (target == null) {
                player.sendMessage(fileManager.getMsg("reply.error"));
                return true;
            }
            if (!target.isOnline()) {
                player.sendMessage(fileManager.getMsg("offlineplayer"));
                return true;
            }
            final String join = StringUtils.join(args, " ");
            target.sendMessage(fileManager.getMsg("msg.player").replace("{PLAYER}", player.getName()).replace("{MESSAGE}", join));
            player.sendMessage(fileManager.getMsg("msg.sender").replace("{PLAYER}", target.getName()).replace("{MESSAGE}", join));
            final List<Player> socialspy = msgManager.getSocialspy();
            for (final Player socialPlayer : socialspy) {
                socialPlayer.sendMessage(fileManager.getMsg("socialspy.socialspy").replace("{PLAYER1}", player.getName()).replace("{PLAYER2}", target.getName()).replace("{MESSAGE}", join));
            }
        }
        return true;
    }
}