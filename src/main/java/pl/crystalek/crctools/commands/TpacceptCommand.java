package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.exceptions.TeleportingPlayerListEmptyException;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.TpaManager;
import pl.crystalek.crctools.managers.UserManager;
import pl.crystalek.crctools.model.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TpacceptCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final CrCTools crCTools;
    private final TpaManager tpaManager;
    private final UserManager userManager;

    public TpacceptCommand(final FileManager fileManager, final CrCTools crCTools, final TpaManager tpaManager, final UserManager userManager) {
        this.fileManager = fileManager;
        this.crCTools = crCTools;
        this.tpaManager = tpaManager;
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (!sender.hasPermission(fileManager.getPermission("tpaccept.tpaccept"))) {
            sender.sendMessage(fileManager.getMsgPermission("tpaccept.tpaccept"));
            return true;
        }
        final Player player = (Player) sender;
        final User user = userManager.getUser(player);
        final List<Player> playerToTp;
        final Map<UUID, Boolean> mapPlayerToTp;
        try {
            mapPlayerToTp = tpaManager.getPlayerToTp(user);
            playerToTp = tpaManager.getPlayerToTp(user).keySet().stream().map(Bukkit::getPlayer).collect(Collectors.toList());
        } catch (final TeleportingPlayerListEmptyException exception) {
            sender.sendMessage(fileManager.getMsg("tpaccept.clearlist"));
            return true;
        }
        if (args.length == 0) {
            if (!(playerToTp.size() == 1)) {
                final String playerToList = playerToTp.stream().map(Player::getName).collect(Collectors.joining(fileManager.getMsg("interlude")));
                sender.sendMessage(fileManager.getMsg("tpaccept.teleportlist").replace("{PLAYERS}", playerToList));
                return true;
            }
            if (playerToTp.get(0).isOnline()) {
                final Player player0ToTp = playerToTp.get(0);
                teleportPlayer(player, player0ToTp, mapPlayerToTp.get(player0ToTp.getUniqueId()));
            } else {
                sender.sendMessage(fileManager.getMsg("offlineplayer"));
            }
        } else if (args.length == 1) {
            if (args[0].equals("*")) {
                for (final Player value : playerToTp) {
                    if (value.isOnline()) {
                        teleportPlayer(player, value, mapPlayerToTp.get(value.getUniqueId()));
                    }
                }
            } else {
                if (Bukkit.getPlayer(args[0]) == null) {
                    sender.sendMessage(fileManager.getMsg("offlineplayer"));
                    return true;
                }
                final Player target = Bukkit.getPlayer(args[0]);
                teleportPlayer(player, target, mapPlayerToTp.get(target.getUniqueId()));
            }
        } else {
            sender.sendMessage(fileManager.getMsg("tpaccept.usage"));
        }
        return true;
    }

    private void teleportPlayer(final Player player, final Player target, final boolean option) {
        Player playerToTp = player;
        Player targetToTp = target;
        if (option) {
            playerToTp = target;
            targetToTp = player;
        }
        if (tpaManager.removeTeleport(userManager.getUser(player), userManager.getUser(target))) {
            target.sendMessage(fileManager.getMsg("tpaccept.teleport").replace("{PLAYER}", player.getName()).replace("{TIME}", String.valueOf(fileManager.getInt("teleporttime"))));
            player.sendMessage(fileManager.getMsg("tpaccept.teleportsender").replace("{PLAYER}", target.getName()));
            teleportTimer(playerToTp, targetToTp);
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
                    target.sendMessage(fileManager.getMsg("teleport.counter").replace("{TIME}", String.valueOf(fileManager.getInt("teleporttime") - i)));
                    i++;
                } else {
                    target.sendMessage(fileManager.getMsg("teleport.error"));
                    cancel();
                }

                if (i == fileManager.getInt("teleporttime")) {
                    if (player.isOnline()) {
                        sender = player.getLocation();
                    }
                    target.teleport(sender);
                    target.sendMessage(fileManager.getMsg("teleport.success"));
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(crCTools, 0L, 20L);
    }
}