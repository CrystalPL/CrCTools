package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.exceptions.TeleportingPlayerListEmpty;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.TpaManager;
import pl.crystalek.crctools.utils.ChatUtil;

import java.util.List;
import java.util.stream.Collectors;

public class TpacceptCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final CrCTools crCTools;
    private final TpaManager tpaManager;

    public TpacceptCommand(final FileManager fileManager, final CrCTools crCTools, final TpaManager tpaManager) {
        this.fileManager = fileManager;
        this.crCTools = crCTools;
        this.tpaManager = tpaManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        final Player player = (Player) sender;
        final List<Player> playerToTp;
        try {
            playerToTp = tpaManager.getPlayerToTp(player.getUniqueId());
        } catch (TeleportingPlayerListEmpty exception) {
            sender.sendMessage(fileManager.getMsg("tpaccept.clearlist"));
            return true;
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
                for (final Player value : playerToTp) {
                    if (value.isOnline()) {
                        teleportPlayer(player, value);
                    }
                }
            } else {
                if (Bukkit.getPlayer(args[0]) == null) {
                    sender.sendMessage(fileManager.getMsg("offlineplayer"));
                    return true;
                }
                final Player target = Bukkit.getPlayer(args[0]);
                teleportPlayer(player, target);
            }
        } else {
            sender.sendMessage(fileManager.getMsg("tpaccept.usage"));
        }
        return true;
    }

    private void teleportPlayer(final Player player, final Player target) {
        if (tpaManager.removeTeleport(player.getUniqueId(), target)) {
            target.sendMessage(fileManager.getMsg("tpaccept.teleport").replace("{PLAYER}", player.getName()).replace("{TIME}", String.valueOf(fileManager.getInt("teleporttime"))));
            player.sendMessage(fileManager.getMsg("tpaccept.teleportsender").replace("{PLAYER}", target.getName()));
            teleportTimer(player, target);
        }
    }

    private void teleportTimer(final Player player, final Player target) {
        new BukkitRunnable() {
            private final Location location = target.getLocation();
            private Location sender = player.getLocation();
            private int i;

            public void run() {
                if (!target.isOnline()) {
                    cancel();
                }

                final Location newLocation = target.getLocation();
                if (location.getX() == newLocation.getX() && location.getY() == newLocation.getY() && location.getZ() == newLocation.getZ()) {
                    player.sendMessage(fileManager.getMsg("warp.warp").replace("{TIME}", String.valueOf(fileManager.getInt("teleporttime") - i)));
                    i++;
                } else {
                    target.sendMessage(fileManager.getMsg("tpaccept.error"));
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