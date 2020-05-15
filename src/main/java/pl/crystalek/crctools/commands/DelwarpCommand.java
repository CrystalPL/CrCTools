package pl.crystalek.crctools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.WarpManager;

public class DelwarpCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final WarpManager warpManager;

    public DelwarpCommand(final FileManager fileManager, final WarpManager warpManager) {
        this.fileManager = fileManager;
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("warp.delwarp"))) {
            sender.sendMessage(fileManager.getMsgPermission("warp.delwarp"));
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(fileManager.getMsg("delwarp.usage"));
            return true;
        }
        if (!warpManager.checkWarp(args[0])) {
            sender.sendMessage(fileManager.getMsg("delwarp.warpexist"));
            return true;
        }
        warpManager.removeWarp(args[0]);
        sender.sendMessage(fileManager.getMsg("delwarp.delete"));
        return true;
    }
}