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

public class TpaCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final CrCTools crCTools;
    private final TpaManager tpaManager;
    private final UserManager userManager;

    public TpaCommand(final FileManager fileManager, final CrCTools crCTools, final TpaManager tpaManager, final UserManager userManager) {
        this.fileManager = fileManager;
        this.crCTools = crCTools;
        this.tpaManager = tpaManager;
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
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
        if (!userManager.getUser(target).isTpa()) {
            player.sendMessage(fileManager.getMsg("tpa.block"));
            return true;
        }
        if (tpaManager.checkTeleport(player.getUniqueId(), player)) {
            sender.sendMessage(fileManager.getMsg("tpa.erroradd"));
            return true;
        }
        if (player.equals(target)) {
            sender.sendMessage(fileManager.getMsg("tpa.error"));
            return true;
        }
        tpaManager.addTeleport(target.getUniqueId(), player);
        player.sendMessage(fileManager.getMsg("tpa.sender").replace("{PLAYER}", target.getName()));
        Bukkit.getScheduler().scheduleAsyncDelayedTask(crCTools, () -> {
            if (tpaManager.removeTeleport(target.getUniqueId(), player)) {
                player.sendMessage(fileManager.getMsg("tpa.barred"));
            }

        }, fileManager.getInt("acceptteleport") * 20L);
        for (final String string : fileManager.getMsgList("tpa.player")) {
            target.sendMessage(string.replace("{PLAYER}", player.getName()).replace("{TIME}", String.valueOf(fileManager.getInt("acceptteleport"))));
        }
        return true;
    }
}