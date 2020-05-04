package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.exceptions.TeleportingPlayerNotExist;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.ChatUtil;
import pl.crystalek.crctools.utils.TpaUtil;

import java.util.List;
import java.util.stream.Collectors;

public class TpdenyCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final TpaUtil tpaUtil;

    public TpdenyCommand(FileManager fileManager, final TpaUtil tpaUtil) {
        this.fileManager = fileManager;
        this.tpaUtil = tpaUtil;
    }

    @Override
    @Deprecated
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tpdeny")) {
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
                    if (playerToTp.size() == 1) {
                        denyPlayer(player, playerToTp.get(0));
                    } else {
                        final String playerToList = playerToTp.stream().map(Player::getName).collect(Collectors.joining(ChatUtil.fixColor("&7, &6")));
                        sender.sendMessage(fileManager.getMsg("tpaccept.teleportlist").replace("{PLAYERS}", playerToList));
                        return true;
                    }
                } else if (args.length == 1) {
                    if (args[0].equals("*")) {
                        for (Player value : playerToTp) {
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
            } else {
                sender.sendMessage(fileManager.getMsg("notconsole"));
            }
        }
        return true;
    }

    private void denyPlayer(Player player, Player target) {
        tpaUtil.removeTeleport(player.getUniqueId(), target);
        player.sendMessage(fileManager.getMsg("tpdeny.tpdeny").replace("{PLAYER}", target.getName()));
        if (target.isOnline()) {
            target.sendMessage(fileManager.getMsg("tpdeny.tpdenyplayer"));
        }
    }
}
