package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.exceptions.TeleportingPlayerNotExist;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.model.TpaManager;
import pl.crystalek.crctools.utils.ChatUtil;

import java.util.List;
import java.util.stream.Collectors;

public class TpacceptCommand implements CommandExecutor {
    private final TpaManager tpaManager;
    private final FileManager fileManager;

    public TpacceptCommand(final FileManager fileManager, final TpaManager tpaManager) {
        this.tpaManager = tpaManager;
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tpaccept")) {
            if (sender instanceof Player) {
                final Player player = (Player) sender;
                List<Player> playerToTp = null;
                try {
                    playerToTp = tpaManager.getPlayerToTp(player.getUniqueId());
                } catch (TeleportingPlayerNotExist exception) {
                    sender.sendMessage(fileManager.getMsg("tpaccept.clearlist"));
                    return false;
                }
                if (args.length == 0) {
                    if (playerToTp.size() == 1) {
                        if (playerToTp.get(0).isOnline()) {
                            Player target = playerToTp.get(0);
                            if (tpaManager.removeTeleport(player.getUniqueId(), target)) {
                                target.sendMessage(fileManager.getMsg("tpaccept.teleport").replace("{PLAYER}", player.getName()).replace("{TIME}", String.valueOf(fileManager.getInt("teleporttime"))));
                                sender.sendMessage(fileManager.getMsg("tpaccept.teleportsender").replace("{PLAYER}", target.getName()));
                            }
                        } else {
                            sender.sendMessage(fileManager.getMsg("offlineplayer"));
                        }
                    } else {
                        final String playerToList = playerToTp.stream().map(Player::getName).collect(Collectors.joining(ChatUtil.fixColor("&7, &6")));
                        sender.sendMessage(fileManager.getMsg("tpaccept.teleportlist").replace("{PLAYERS}", playerToList));
                    }
                } else if (args.length == 1) {
                    if (args[0].equals("*")) {
                        for (Player value : playerToTp) {
                            if (value.isOnline()) {
                                if (tpaManager.removeTeleport(player.getUniqueId(), value)) {
                                    value.sendMessage(fileManager.getMsg("tpaccept.teleport").replace("{PLAYER}", player.getName()).replace("{TIME}", String.valueOf(fileManager.getInt("teleporttime"))));
                                    sender.sendMessage(fileManager.getMsg("tpaccept.teleportsender").replace("{PLAYER}", value.getName()));
                                }
                            }
                        }
                        sender.sendMessage(fileManager.getMsg("tpaccept.completeteleport"));
                    } else {
                        if (Bukkit.getPlayer(args[0]) != null) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (tpaManager.removeTeleport(player.getUniqueId(), target)) {
                                target.sendMessage(fileManager.getMsg("tpaccept.teleport").replace("{PLAYER}", player.getName()).replace("{TIME}", String.valueOf(fileManager.getInt("teleporttime"))));
                                sender.sendMessage(fileManager.getMsg("tpaccept.teleportsender").replace("{PLAYER}", target.getName()));
                            } else {
                                sender.sendMessage(fileManager.getMsg("tpaccept.playerclearlist"));
                            }
                        } else {
                            sender.sendMessage(fileManager.getMsg("offlineplayer"));
                        }
                    }
                } else {
                    sender.sendMessage(fileManager.getMsg("tpaccept.usage"));
                }
            } else {
                sender.sendMessage(fileManager.getMsg("notconsole"));
            }
        }
        return true;
    }
}