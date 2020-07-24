package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.TpaManager;
import pl.crystalek.crctools.managers.UserManager;
import pl.crystalek.crctools.model.User;

public class TpahereCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final UserManager userManager;
    private final TpaManager tpaManager;
    private final CrCTools crCTools;

    public TpahereCommand(final FileManager fileManager, final UserManager userManager, final TpaManager tpaManager, final CrCTools crCTools) {
        this.fileManager = fileManager;
        this.userManager = userManager;
        this.tpaManager = tpaManager;
        this.crCTools = crCTools;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (!sender.hasPermission(fileManager.getPermission("tpahere.tpahere"))) {
            sender.sendMessage(fileManager.getMsgPermission("tpahere.tpahere"));
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(fileManager.getMsg("tpa.usage"));
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(fileManager.getMsg("offlineplayer"));
            return true;
        }
        final Player target = Bukkit.getPlayer(args[0]);
        final Player player = (Player) sender;
        final User targetUser = userManager.getUser(target);
        final User senderUser = userManager.getUser(player);
        if (!targetUser.isTpa()) {
            player.sendMessage(fileManager.getMsg("tpa.block"));
            return true;
        }
        if (tpaManager.checkTeleport(targetUser, senderUser)) {
            sender.sendMessage(fileManager.getMsg("tpa.erroradd"));
            return true;
        }
        if (player.equals(target)) {
            sender.sendMessage(fileManager.getMsg("tpa.error"));
            return true;
        }
        tpaManager.addTeleport(targetUser, senderUser, true);
        player.sendMessage(fileManager.getMsg("tpahere.sender").replace("{PLAYER}", target.getName()));
        Bukkit.getScheduler().scheduleAsyncDelayedTask(crCTools, () -> {
            if (tpaManager.removeTeleport(targetUser, senderUser)) {
                player.sendMessage(fileManager.getMsg("tpa.barred"));
            }

        }, fileManager.getInt("acceptteleport") * 20L);
        for (final String string : fileManager.getMsgList("tpahere.player")) {
            target.sendMessage(string.replace("{PLAYER}", player.getName()).replace("{TIME}", String.valueOf(fileManager.getInt("acceptteleport"))));
        }
        return true;
    }
}
