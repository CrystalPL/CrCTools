package pl.crystalek.crctools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.WarpManager;

public final class SetwarpCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final WarpManager warpManager;

    public SetwarpCommand(final FileManager fileManager, final WarpManager warpManager) {
        this.fileManager = fileManager;
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (!sender.hasPermission(fileManager.getPermission("warp.setwarp"))) {
            sender.sendMessage(fileManager.getMsgPermission("warp.setwarp"));
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(fileManager.getMsg("setwarp.usage"));
            return true;
        }
        sender.sendMessage(fileManager.getMsg("setwarp.create"));
        warpManager.addWarp(args[0], (Player) sender);
        return true;
    }
}