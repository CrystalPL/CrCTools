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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("exp")) {
            if (sender.hasPermission(fileManager.getPermission("exp.exp"))) {
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("set")) {
                        if (sender instanceof Player) {
                            if (NumberUtil.isInt(args[1])) {
                                if (Integer.parseInt(args[1]) > 0) {
                                    Player player = (Player) sender;
                                    switch (args[0].toLowerCase()) {
                                        case "give":
                                            int level = Integer.parseInt(args[1]);
                                            player.setLevel(player.getLevel() + level);
                                            player.sendMessage(fileManager.getMsg("exp.give").replace("{LEVELCHANGE}", String.valueOf(level)).replace("{LEVEL}", String.valueOf(player.getLevel())));
                                            break;
                                        case "take":
                                            int level2 = Integer.parseInt(args[1]);
                                            if (player.getLevel() - level2 >= 0) {
                                                player.setLevel(player.getLevel() - level2);
                                                player.sendMessage(fileManager.getMsg("exp.take").replace("{LEVELCHANGE}", String.valueOf(level2)).replace("{LEVEL}", String.valueOf(player.getLevel())));
                                            } else {
                                                player.sendMessage(fileManager.getMsg("exp.errortake"));
                                            }
                                            break;
                                        case "set":
                                            int level3 = Integer.parseInt(args[1]);
                                            player.setLevel(level3);
                                            player.sendMessage(fileManager.getMsg("exp.set").replace("{LEVELCHANGE}", String.valueOf(level3)));
                                            break;
                                    }
                                } else {
                                    sender.sendMessage(fileManager.getMsg("exp.error"));
                                }
                            } else {
                                sender.sendMessage(fileManager.getMsg("exp.usage"));
                            }
                        } else {
                            sender.sendMessage(fileManager.getMsg("notconsole"));
                        }
                    } else if (args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("reset")) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            Player player = Bukkit.getPlayer(args[1]);
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
                            sender.sendMessage(fileManager.getMsg("offlineplayer"));
                        }
                    } else {
                        sender.sendMessage(fileManager.getMsg("exp.usage"));
                    }
                } else if (args.length == 3) {
                    if (sender.hasPermission(fileManager.getPermission("exp.player"))) {
                        if (NumberUtil.isInt(args[1])) {
                            if (Integer.parseInt(args[1]) > 0) {
                                if (Bukkit.getPlayer(args[2]) != null) {
                                    Player player = Bukkit.getPlayer(args[2]);
                                    switch (args[0].toLowerCase()) {
                                        case "give":
                                            int level = Integer.parseInt(args[1]);
                                            sender.sendMessage(fileManager.getMsg("exp.giveplayer").replace("{LEVELCHANGE}", String.valueOf(level + player.getLevel())).replace("{PLAYER}", player.getName()).replace("{LEVEL}", String.valueOf(player.getLevel())));
                                            player.setLevel(player.getLevel() + level);
                                            player.sendMessage(fileManager.getMsg("exp.give").replace("{LEVELCHANGE}", String.valueOf(level)).replace("{LEVEL}", String.valueOf(player.getLevel())));
                                            break;
                                        case "take":
                                            int level2 = Integer.parseInt(args[1]);
                                            if (player.getLevel() - level2 >= 0) {
                                                sender.sendMessage(fileManager.getMsg("exp.takeplayer").replace("{LEVELCHANGE}", String.valueOf(player.getLevel() - level2)).replace("{PLAYER}", player.getName()).replace("{LEVEL}", String.valueOf(player.getLevel())));
                                                player.setLevel(player.getLevel() - level2);
                                                player.sendMessage(fileManager.getMsg("exp.take").replace("{LEVELCHANGE}", String.valueOf(level2)).replace("{LEVEL}", String.valueOf(player.getLevel())));
                                            } else {
                                                player.sendMessage(fileManager.getMsg("exp.errortake"));
                                            }
                                            break;
                                        case "set":
                                            int level3 = Integer.parseInt(args[1]);
                                            player.setLevel(level3);
                                            player.sendMessage(fileManager.getMsg("exp.set").replace("{LEVELCHANGE}", String.valueOf(level3)));
                                            sender.sendMessage(fileManager.getMsg("exp.setplayer").replace("{PLAYER}", player.getName()).replace("{LEVELCHANGE}", String.valueOf(level3)));
                                            break;
                                        default:
                                            sender.sendMessage(fileManager.getMsg("exp.usage"));
                                    }
                                } else {
                                    sender.sendMessage(fileManager.getMsg("offlineplayer"));
                                }
                            } else {
                                sender.sendMessage(fileManager.getMsg("exp.error"));
                            }
                        } else {
                            sender.sendMessage(fileManager.getMsg("exp.usage"));
                        }
                    } else {
                        sender.sendMessage(fileManager.getMsgPermission("exp.player"));
                    }
                } else {
                    sender.sendMessage(fileManager.getMsg("exp.usage"));
                }
            } else {
                sender.sendMessage(fileManager.getMsgPermission("exp.exp"));
            }
        }
        return true;
    }
}