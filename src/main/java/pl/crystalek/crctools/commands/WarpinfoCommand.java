package pl.crystalek.crctools.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.WarpManager;
import pl.crystalek.crctools.model.Warp;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class WarpinfoCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final WarpManager warpManager;
    private final DecimalFormat decimalFormat;

    public WarpinfoCommand(final FileManager fileManager, final WarpManager warpManager, final DecimalFormat decimalFormat) {
        this.fileManager = fileManager;
        this.warpManager = warpManager;
        this.decimalFormat = decimalFormat;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("warp.info"))) {
            sender.sendMessage(fileManager.getMsgPermission("warp.info"));
            return true;
        }
        if (warpManager.printWarpList(sender, args, fileManager)) return true;

        final List<String> msgList = fileManager.getMsgList("warpinfo.info");
        final Warp warp = warpManager.getWarp(args[0]);
        final Location location = warp.getLocation();
        for (final String msg : msgList) {
            sender.sendMessage(msg
                    .replace("{WARP}", args[0])
                    .replace("{TIME}", warp.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .replace("{AUTHOR}", warp.getAuthor())
                    .replace("{WORLD}", location.getWorld().getName())
                    .replace("{X}", decimalFormat.format(location.getX()))
                    .replace("{Y}", decimalFormat.format(location.getY()))
                    .replace("{Z}", decimalFormat.format(location.getZ())));
        }
        return true;
    }
}
