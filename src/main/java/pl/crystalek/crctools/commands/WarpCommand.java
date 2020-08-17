package pl.crystalek.crctools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.WarpManager;
import pl.crystalek.crctools.utils.TeleportUtil;

public final class WarpCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final WarpManager warpManager;
    private final CrCTools crCTools;

    public WarpCommand(final FileManager fileManager, final WarpManager warpManager, final CrCTools crCTools) {
        this.fileManager = fileManager;
        this.warpManager = warpManager;
        this.crCTools = crCTools;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (warpManager.printWarpList(sender, args, fileManager)) return true;

        TeleportUtil.teleportTimer((Player) sender, warpManager.getWarp(args[0]).getLocation(), fileManager, crCTools);
        return true;
    }
}