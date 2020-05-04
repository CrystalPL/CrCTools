package pl.crystalek.crctools.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.model.MsgManager;
import pl.crystalek.crctools.model.UserManager;

import java.util.List;

public class MsgCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final MsgManager msgManager;
    private final UserManager userManager;

    public MsgCommand(final FileManager fileManager, final MsgManager msgManager, final UserManager userManager) {
        this.fileManager = fileManager;
        this.msgManager = msgManager;
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("msg")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return false;
            }
            Player player = (Player) sender;
            if (args.length <= 1) {
                player.sendMessage(fileManager.getMsg("msg.usage"));
                return false;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                player.sendMessage(fileManager.getMsg("offlineplayer"));
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (!userManager.getUser(target).isMsg()) {
                player.sendMessage(fileManager.getMsg("msg.block"));
                return false;
            }
            final String join = StringUtils.join(args, ' ', 1, args.length);
            target.sendMessage(fileManager.getMsg("msg.player").replace("{PLAYER}", player.getName()).replace("{MESSAGE}", join));
            player.sendMessage(fileManager.getMsg("msg.sender").replace("{PLAYER}", target.getName()).replace("{MESSAGE}", join));
            msgManager.addMsg(player, target);

            final List<Player> socialspy = msgManager.getSocialspy();
            for (Player socialPlayer : socialspy) {
                socialPlayer.sendMessage(fileManager.getMsg("socialspy.socialspy").replace("{PLAYER1}", player.getName()).replace("{PLAYER2}", target.getName()).replace("{MESSAGE}", join));
            }
        }
        return true;
    }
}
