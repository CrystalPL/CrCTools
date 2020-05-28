package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.NumberUtil;

public class ExpCommand implements CommandExecutor {
    private final FileManager fileManager;

    public ExpCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 2) {
            if (!sender.hasPermission(fileManager.getPermission("exp.exp"))) {
                sender.sendMessage(fileManager.getMsgPermission("exp.exp"));
                return true;
            }
            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("set")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(fileManager.getMsg("notconsole"));
                    return true;
                }
                if (checkNumber(sender, args)) return true;
                final Player player = (Player) sender;
                final int level = Integer.parseInt(args[1]);
                switch (args[0].toLowerCase()) {
                    case "give":
                        player.setLevel(player.getLevel() + level);
                        player.sendMessage(fileManager.getMsg("exp.give").replace("{LEVELCHANGE}", String.valueOf(level)).replace("{LEVEL}", String.valueOf(player.getLevel())));
                        break;
                    case "take":
                        if (player.getLevel() - level >= 0) {
                            player.setLevel(player.getLevel() - level);
                            player.sendMessage(fileManager.getMsg("exp.take").replace("{LEVELCHANGE}", String.valueOf(level)).replace("{LEVEL}", String.valueOf(player.getLevel())));
                        } else {
                            player.sendMessage(fileManager.getMsg("exp.errortake"));
                        }
                        break;
                    case "set":
                        player.setLevel(level);
                        player.sendMessage(fileManager.getMsg("exp.set").replace("{LEVELCHANGE}", String.valueOf(level)));
                        break;
                }
            } else if (args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("reset")) {
                if (Bukkit.getPlayer(args[1]) == null) {
                    sender.sendMessage(fileManager.getMsg("offlineplayer"));
                    return true;
                }
                final Player player = Bukkit.getPlayer(args[1]);
                switch (args[0].toLowerCase()) {
                    case "show":
                        sender.sendMessage(fileManager.getMsg("exp.show").replace("{LEVEL}", String.valueOf(player.getLevel())).replace("{PLAYER}", player.getName()));
                        break;
                    case "reset":
                        player.setLevel(0);
                        player.sendMessage(fileManager.getMsg("exp.reset"));
                        sender.sendMessage(fileManager.getMsg("exp.resetplayer").replace("{PLAYER}", player.getName()));
                        break;
                }
            } else {
                sender.sendMessage(fileManager.getMsg("exp.usage"));
            }
        } else if (args.length == 3) {
            if (!sender.hasPermission(fileManager.getPermission("exp.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("exp.player"));
                return true;
            }
            if (checkNumber(sender, args)) return true;
            if (Bukkit.getPlayer(args[2]) == null) {
                sender.sendMessage(fileManager.getMsg("offlineplayer"));
                return true;
            }
            final Player player = Bukkit.getPlayer(args[2]);
            final int level = Integer.parseInt(args[1]);
            switch (args[0].toLowerCase()) {
                case "give":
                    sender.sendMessage(fileManager.getMsg("exp.giveplayer").replace("{LEVELCHANGE}", String.valueOf(level + player.getLevel())).replace("{PLAYER}", player.getName()).replace("{LEVEL}", String.valueOf(player.getLevel())));
                    player.setLevel(player.getLevel() + level);
                    player.sendMessage(fileManager.getMsg("exp.give").replace("{LEVELCHANGE}", String.valueOf(level)).replace("{LEVEL}", String.valueOf(player.getLevel())));
                    break;
                case "take":
                    if (player.getLevel() - level >= 0) {
                        sender.sendMessage(fileManager.getMsg("exp.takeplayer").replace("{LEVELCHANGE}", String.valueOf(player.getLevel() - level)).replace("{PLAYER}", player.getName()).replace("{LEVEL}", String.valueOf(player.getLevel())));
                        player.setLevel(player.getLevel() - level);
                        player.sendMessage(fileManager.getMsg("exp.take").replace("{LEVELCHANGE}", String.valueOf(level)).replace("{LEVEL}", String.valueOf(player.getLevel())));
                    } else {
                        sender.sendMessage(fileManager.getMsg("exp.errortake"));
                    }
                    break;
                case "set":
                    player.setLevel(level);
                    player.sendMessage(fileManager.getMsg("exp.set").replace("{LEVELCHANGE}", String.valueOf(level)));
                    sender.sendMessage(fileManager.getMsg("exp.setplayer").replace("{PLAYER}", player.getName()).replace("{LEVELCHANGE}", String.valueOf(level)));
                    break;
                default:
                    sender.sendMessage(fileManager.getMsg("exp.usage"));
            }
        } else {
            sender.sendMessage(fileManager.getMsg("exp.usage"));
        }
        return true;
    }

    private boolean checkNumber(final CommandSender sender, final String[] args) {
        if (!NumberUtil.isInt(args[1])) {
            sender.sendMessage(fileManager.getMsg("speed.errornumber"));
            return true;
        }
        if (!(Integer.parseInt(args[1]) > 0)) {
            sender.sendMessage(fileManager.getMsg("exp.error"));
            return true;
        }
        return false;
    }
}