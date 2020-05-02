package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.TpaUtil;

public class TpaCommand implements CommandExecutor {
    private FileManager fileManager;
    private CrCTools crCTools;

    public TpaCommand(final FileManager fileManager, CrCTools crCTools) {
        this.fileManager = fileManager;
        this.crCTools = crCTools;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tpa")) {
            if (sender instanceof Player) {
                if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        Player player = Bukkit.getPlayer(args[0]);
                        Player target = (Player) sender;
                        if (!TpaUtil.checkTeleport(player.getUniqueId(), target)) {
                            if (!target.equals(player)) {
                                TpaUtil.addTeleport(player.getUniqueId(), target);
                                target.sendMessage(fileManager.getMsg("tpa.sender").replace("{PLAYER}", player.getName()));
                                Bukkit.getScheduler().scheduleSyncDelayedTask(crCTools, () -> {
                                    if (TpaUtil.removeTeleport(player.getUniqueId(), target)) {
                                        sender.sendMessage(fileManager.getMsg("tpa.barred"));
                                    }

                                }, fileManager.getInt("acceptteleport") * 20L);
                                for (String string : fileManager.getMsgList("tpa.player")) {
                                    player.sendMessage(string.replace("{PLAYER}", target.getName()).replace("{TIME}", String.valueOf(fileManager.getInt("acceptteleport"))));
                                }
                            } else {
                                sender.sendMessage(fileManager.getMsg("tpa.error"));
                            }
                        } else {
                            sender.sendMessage(fileManager.getMsg("tpa.erroradd"));
                        }
                    } else {
                        sender.sendMessage(fileManager.getMsg("offlineplayer"));
                    }
                } else {
                    sender.sendMessage(fileManager.getMsg("tpa.usage"));
                }
            } else {
                sender.sendMessage(fileManager.getMsg("notconsole"));
            }
        }
        return true;
    }
}
