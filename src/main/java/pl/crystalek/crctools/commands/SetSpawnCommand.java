package pl.crystalek.crctools.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;

import java.text.DecimalFormat;

public class SetSpawnCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final DecimalFormat decimalFormat;
    private final CrCTools crCTools;

    public SetSpawnCommand(final FileManager fileManager, final DecimalFormat decimalFormat, final CrCTools crCTools) {
        this.fileManager = fileManager;
        this.decimalFormat = decimalFormat;
        this.crCTools = crCTools;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (args.length != 0) {
            sender.sendMessage(fileManager.getMsg("setspawn.usage"));
            return true;
        }
        final Player player = (Player) sender;
        if (!player.hasPermission(fileManager.getPermission("setspawn.setspawn"))) {
            player.sendMessage(fileManager.getMsgPermission("setspawn.setspawn"));
            return true;
        }
        final FileConfiguration configuration = crCTools.getConfig();
        final Location location = player.getLocation();
        final String string = "spawn";
        configuration.set(string + ".world", location.getWorld().getName());
        configuration.set(string + ".x", decimalFormat.format(location.getX()));
        configuration.set(string + ".y", decimalFormat.format(location.getY()));
        configuration.set(string + ".z", decimalFormat.format(location.getZ()));
        crCTools.saveConfig();
        for (final String message : fileManager.getMsgList("setspawn.setspawn")) {
            player.sendMessage(message.replace("{WORLD}", location.getWorld().getName()).replace("{X}", String.valueOf(location.getX())).replace("{Y}", String.valueOf(location.getY())).replace("{Z}", String.valueOf(location.getZ())));
        }
        return true;
    }
}