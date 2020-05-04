package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.exceptions.TeleportingPlayerNotExist;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.ChatUtil;
import pl.crystalek.crctools.utils.TpaUtil;

import java.util.List;
import java.util.stream.Collectors;

public class TpacceptCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final CrCTools crCTools;
    private final TpaUtil tpaUtil;

    public TpacceptCommand(final FileManager fileManager, final CrCTools crCTools, final TpaUtil tpaUtil) {
        this.fileManager = fileManager;
        this.crCTools = crCTools;
        this.tpaUtil = tpaUtil;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tpaccept")) {
            if (sender instanceof Player) {
                final Player player = (Player) sender;
                List<Player> playerToTp;
                try {
                    playerToTp = tpaUtil.getPlayerToTp(player.getUniqueId());
                } catch (TeleportingPlayerNotExist exception) {
                    sender.sendMessage(fileManager.getMsg("tpaccept.clearlist"));
                    return false;
                }
                if (args.length == 0) {
                    if (!(playerToTp.size() == 1)) {
                        final String playerToList = playerToTp.stream().map(Player::getName).collect(Collectors.joining(ChatUtil.fixColor("&7, &6")));
                        sender.sendMessage(fileManager.getMsg("tpaccept.teleportlist").replace("{PLAYERS}", playerToList));
                        return true;
                    }
                    if (playerToTp.get(0).isOnline()) {
                        teleportPlayer(player, playerToTp.get(0));
                    } else {
                        sender.sendMessage(fileManager.getMsg("offlineplayer"));
                    }

                } else if (args.length == 1) {
                    if (args[0].equals("*")) {
                        for (Player value : playerToTp) {
                            if (value.isOnline()) {
                                teleportPlayer(player, value);
                            }
                        }
                        sender.sendMessage(fileManager.getMsg("tpaccept.completeteleport"));
                    } else {
                        if (Bukkit.getPlayer(args[0]) != null) {
                            Player target = Bukkit.getPlayer(args[0]);
                            teleportPlayer(player, target);
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

    private void teleportPlayer(Player player, Player target) {
        if (tpaUtil.removeTeleport(player.getUniqueId(), target)) {
            target.sendMessage(fileManager.getMsg("tpaccept.teleport").replace("{PLAYER}", player.getName()).replace("{TIME}", String.valueOf(fileManager.getInt("teleporttime"))));
            player.sendMessage(fileManager.getMsg("tpaccept.teleportsender").replace("{PLAYER}", target.getName()));
            teleportTimer(player, target);
        }
    }

    private void teleportTimer(Player player, Player target) {
        new BukkitRunnable() {
            Location location = target.getLocation();
            Location sender = player.getLocation();
            private int i;

            public void run() {
                if (target.isOnline()) {
                    Location newLocation = target.getLocation();
                    if (location.getX() == newLocation.getX() && location.getY() == newLocation.getY() && location.getZ() == newLocation.getZ()) {
                        i++;
                    } else {
                        target.sendMessage(fileManager.getMsg("tpaccept.error"));
                        cancel();
                    }
                } else {
                    cancel();
                }
                if (i == fileManager.getInt("teleporttime")) {
                    if (player.isOnline()) {
                        sender = player.getLocation();
                    }
                    target.teleport(sender);
                    target.sendMessage(fileManager.getMsg("tpaccept.success"));
                    cancel();
                }
            }
        }.runTaskTimer(crCTools, 0L, 20L);
    }
}