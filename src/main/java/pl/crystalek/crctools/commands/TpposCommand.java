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

public class TpposCommand implements CommandExecutor {
    FileManager fileManager = new FileManager();

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("tppos")) {
            if (args.length == 4) {
                if (sender instanceof Player) {
                    if (sender.hasPermission(fileManager.getPermission("tppos.tppos"))) {
                        Player player = (Player) sender;
                        try {
                            teleportPlayer(args, player);
                        } catch (CommandException | IllegalArgumentException exception) {
                            sender.sendMessage(fileManager.getMsg("tppos.usage"));
                        }
                    } else {
                        sender.sendMessage(fileManager.getMsgPermission("tppos.tppos"));
                    }
                } else {
                    sender.sendMessage(fileManager.getMsg("notconsole"));
                }
            } else if (args.length == 5) {
                if (sender.hasPermission(fileManager.getPermission("tppos.player"))) {
                    if (Bukkit.getPlayer(args[4]) != null) {
                        Player player = Bukkit.getPlayer(args[4]);
                        try {
                            teleportPlayer(args, player);
                            sendMessage((Player) sender, fileManager.getMsgList("tppos.player"));
                        } catch (CommandException | IllegalArgumentException exception) {
                            sender.sendMessage(fileManager.getMsg("tppos.usage"));
                        }
                    } else {
                        sender.sendMessage(fileManager.getMsg("offlineplayer"));
                    }
                } else {
                    sender.sendMessage(fileManager.getMsgPermission("tppos.player"));
                }
            } else {
                sender.sendMessage(fileManager.getMsg("tppos.usage"));
            }
        }
        return true;
    }

    private void teleportPlayer(String[] args, Player player) {
        double x = player.getLocation().getX() - (int) player.getLocation().getX();
        double y = player.getLocation().getY() - (int) player.getLocation().getY();
        double z = player.getLocation().getZ() - (int) player.getLocation().getZ();
        player.teleport(new Location(Bukkit.getServer().getWorld(args[0]), x + Double.parseDouble(args[1]), y + Double.parseDouble(args[2]), z  + Double.parseDouble(args[3]), player.getLocation().getYaw(), player.getLocation().getPitch()));
        final List<String> tppos = fileManager.getMsgList("tppos.tppos");
        sendMessage(player, tppos);
    }

    private void sendMessage(Player player, List<String> lists) {
        final DecimalFormat decimalFormat = new DecimalFormat();
        for (String string : lists) {
            player.sendMessage(string
                    .replace("{WORLD}", player.getWorld().getName())
                    .replace("{X}", decimalFormat.format(player.getLocation().getX()))
                    .replace("{Y}", decimalFormat.format(player.getLocation().getY()))
                    .replace("{Z}", decimalFormat.format(player.getLocation().getZ()))
                    .replace("{PLAYER}", player.getName()));
        }
    }
}
