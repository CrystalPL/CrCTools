package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

import java.text.DecimalFormat;
import java.util.List;

public final class TpposCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final DecimalFormat decimalFormat;

    public TpposCommand(final FileManager fileManager, final DecimalFormat decimalFormat) {
        this.fileManager = fileManager;
        this.decimalFormat = decimalFormat;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 4) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return true;
            }
            final Player player = (Player) sender;
            if (!player.hasPermission(fileManager.getPermission("tppos.tppos"))) {
                player.sendMessage(fileManager.getMsgPermission("tppos.tppos"));
                return true;
            }
            try {
                teleportPlayer(args, player);
            } catch (CommandException | IllegalArgumentException exception) {
                sender.sendMessage(fileManager.getMsg("tppos.usage"));
            }

        } else if (args.length == 5) {
            if (!sender.hasPermission(fileManager.getPermission("tppos.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("tppos.player"));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(fileManager.getMsg("offlineplayer"));
                return true;
            }
            final Player player = Bukkit.getPlayer(args[4]);
            try {
                teleportPlayer(args, player);
                sendMessage((Player) sender, fileManager.getMsgList("tppos.player"));
            } catch (CommandException | IllegalArgumentException exception) {
                sender.sendMessage(fileManager.getMsg("tppos.usage"));
            }
        } else {
            sender.sendMessage(fileManager.getMsg("tppos.usage"));
        }
        return true;
    }

    private void teleportPlayer(final String[] args, final Player player) {
        final double x = player.getLocation().getX() - (int) player.getLocation().getX();
        final double y = player.getLocation().getY() - (int) player.getLocation().getY();
        final double z = player.getLocation().getZ() - (int) player.getLocation().getZ();
        player.teleport(new Location(Bukkit.getServer().getWorld(args[0]), x + Double.parseDouble(args[1]), y + Double.parseDouble(args[2]), z + Double.parseDouble(args[3]), player.getLocation().getYaw(), player.getLocation().getPitch()));
        final List<String> tppos = fileManager.getMsgList("tppos.tppos");
        sendMessage(player, tppos);
    }

    private void sendMessage(final Player player, final List<String> lists) {
        final Location location = player.getLocation();
        for (final String string : lists) {
            player.sendMessage(string
                    .replace("{WORLD}", location.getWorld().getName())
                    .replace("{X}", decimalFormat.format(location.getX()))
                    .replace("{Y}", decimalFormat.format(location.getY()))
                    .replace("{Z}", decimalFormat.format(location.getZ()))
                    .replace("{PLAYER}", player.getName()));
        }
    }
}