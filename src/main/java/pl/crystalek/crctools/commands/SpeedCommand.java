package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.NumberUtil;

public class SpeedCommand implements CommandExecutor {
    FileManager fileManager = new FileManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("speed")) {
            if (sender.hasPermission(fileManager.getPermission("speed.speed"))) {
                if (args.length >= 1 && args.length <= 2) {
                    if (NumberUtil.isInt(args[0])) {
                        float speed = Integer.parseInt(args[0]) / 10F;
                        if (Integer.parseInt(args[0]) >= 0 && Integer.parseInt(args[0]) <= 10) {
                            if (args.length == 2) {
                                if (sender.hasPermission(fileManager.getPermission("speed.player"))) {
                                    if (Bukkit.getPlayer(args[1]) != null) {
                                        Player player = Bukkit.getPlayer(args[1]);
                                        if (player.isFlying()) {
                                            player.setFlySpeed(speed);
                                            player.sendMessage(fileManager.getMsg("speed.fly").replace("{SPEED}", args[0]));
                                            sender.sendMessage(fileManager.getMsg("speed.flyplayer").replace("{SPEED}", args[0]).replace("{PLAYER}", player.getName()));
                                            return true;
                                        } else {
                                            player.setWalkSpeed(speed);
                                            player.sendMessage(fileManager.getMsg("speed.walk").replace("{SPEED}", args[0]));
                                            sender.sendMessage(fileManager.getMsg("speed.walkplayer").replace("{SPEED}", args[0]).replace("{PLAYER}", player.getName()));
                                            return true;
                                        }
                                    } else {
                                        sender.sendMessage(fileManager.getMsg("offlineplayer"));
                                    }
                                } else {
                                    sender.sendMessage(fileManager.getMsgPermission("speed.player"));
                                }
                            }
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                if (player.isFlying()) {
                                    player.setFlySpeed(speed);
                                    player.sendMessage(fileManager.getMsg("speed.fly").replace("{SPEED}", args[0]));
                                } else {
                                    player.setWalkSpeed(speed);
                                    player.sendMessage(fileManager.getMsg("speed.walk").replace("{SPEED}", args[0]));
                                }
                            } else {
                                sender.sendMessage(fileManager.getMsg("notconsole"));
                            }
                        } else {
                            sender.sendMessage(fileManager.getMsg("speed.error"));
                        }
                    } else {
                        sender.sendMessage(fileManager.getMsg("speed.usage"));
                    }
                } else {
                    sender.sendMessage(fileManager.getMsg("speed.usage"));
                }
            } else {
                sender.sendMessage(fileManager.getMsgPermission("speed.speed"));
            }
        }
        return true;
    }
}