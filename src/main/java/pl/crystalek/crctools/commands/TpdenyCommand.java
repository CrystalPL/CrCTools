package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.exceptions.TeleportingPlayerListEmptyException;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.TpaManager;
import pl.crystalek.crctools.managers.UserManager;
import pl.crystalek.crctools.model.User;

import java.util.List;
import java.util.stream.Collectors;

public final class TpdenyCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final TpaManager tpaManager;
    private final UserManager userManager;

    public TpdenyCommand(final FileManager fileManager, final TpaManager tpaManager, final UserManager userManager) {
        this.fileManager = fileManager;
        this.tpaManager = tpaManager;
        this.userManager = userManager;
    }

    @Override
    @Deprecated
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (!sender.hasPermission(fileManager.getPermission("tpdeny.tpdeny"))) {
            sender.sendMessage(fileManager.getMsgPermission("tpdeny.tpdeny"));
            return true;
        }
        final Player player = (Player) sender;
        final User user = userManager.getUser(player);
        final List<Player> playerToTp;
        try {
            playerToTp = tpaManager.getPlayerToTp(user).keySet().stream().map(Bukkit::getPlayer).collect(Collectors.toList());
        } catch (TeleportingPlayerListEmptyException exception) {
            sender.sendMessage(fileManager.getMsg("tpaccept.clearlist"));
            return true;
        }
        if (args.length == 0) {
            if (playerToTp.size() == 1) {
                denyPlayer(player, playerToTp.get(0));
            } else {
                final String playerToList = playerToTp.stream().map(Player::getName).collect(Collectors.joining(fileManager.getMsg("interlude")));
                sender.sendMessage(fileManager.getMsg("tpaccept.teleportlist").replace("{PLAYERS}", playerToList));
            }
        } else if (args.length == 1) {
            if (args[0].equals("*")) {
                for (final Player value : playerToTp) {
                    denyPlayer(player, value);
                }
            } else {
                final Player target;
                if (Bukkit.getPlayer(args[0]) != null) {
                    target = Bukkit.getPlayer(args[0]);
                } else {
                    target = Bukkit.getOfflinePlayer(args[0]).getPlayer();
                }
                denyPlayer(player, target);

            }
        } else {
            sender.sendMessage(fileManager.getMsg("tpdeny.usage"));
        }
        return true;
    }

    private void denyPlayer(final Player player, final Player target) {
        tpaManager.removeTeleport(userManager.getUser(player), userManager.getUser(target));
        player.sendMessage(fileManager.getMsg("tpdeny.tpdeny").replace("{PLAYER}", target.getName()));
        if (target.isOnline()) {
            target.sendMessage(fileManager.getMsg("tpdeny.tpdenyplayer"));
        }
    }
}