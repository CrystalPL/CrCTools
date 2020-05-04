package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.exceptions.TeleportingPlayerListEmpty;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.TpaManager;
import pl.crystalek.crctools.utils.ChatUtil;

import java.util.List;
import java.util.stream.Collectors;

public class TpdenyCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final TpaManager tpaManager;

    public TpdenyCommand(final FileManager fileManager, final TpaManager tpaManager) {
        this.fileManager = fileManager;
        this.tpaManager = tpaManager;
    }

    @Override
    @Deprecated
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
            if (playerToTp.size() == 1) {
                denyPlayer(player, playerToTp.get(0));
            } else {
                final String playerToList = playerToTp.stream().map(Player::getName).collect(Collectors.joining(ChatUtil.fixColor("&7, &6")));
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
        tpaManager.removeTeleport(player.getUniqueId(), target);
        player.sendMessage(fileManager.getMsg("tpdeny.tpdeny").replace("{PLAYER}", target.getName()));
        if (target.isOnline()) {
            target.sendMessage(fileManager.getMsg("tpdeny.tpdenyplayer"));
        }
    }
}