package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

import java.util.function.BiConsumer;

public final class TpCommand implements CommandExecutor {
    private final FileManager fileManager;

    public TpCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return true;
            }
            if (!sender.hasPermission(fileManager.getPermission("tp.tp"))) {
                sender.sendMessage(fileManager.getMsgPermission("tp.tp"));
                return true;
            }
            final Player playerSender = (Player) sender;
            if (playerSender.getName().equalsIgnoreCase(args[0])) {
                sender.sendMessage(fileManager.getMsg("tp.error"));
                return true;
            }
            teleportPlayer(playerSender, args[0], (player, player1) -> {
                player.teleport(player1);
                player.sendMessage(fileManager.getMsg("tp.player").replace("{PLAYER}", player1.getName()));
            });
        } else if (args.length == 2) {
            if (!sender.hasPermission(fileManager.getPermission("tp.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("tp.player"));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(fileManager.getMsg("offlineplayer"));
                return true;
            }
            final Player player1 = Bukkit.getPlayer(args[0]);
            if (player1.getName().equalsIgnoreCase(args[1]) || sender.getName().equalsIgnoreCase(args[0]) || sender.getName().equalsIgnoreCase(args[1])) {
                sender.sendMessage(fileManager.getMsg("tp.usage"));
                return true;
            }
            teleportPlayer(player1, args[1], (player, player2) -> {
                player1.teleport(player2);
                player1.sendMessage(fileManager.getMsg("tp.player").replace("{PLAYER}", player2.getName()));
            });
            sender.sendMessage(fileManager.getMsg("tp.sender").replace("{PLAYER1}", player1.getName()).replace("{PLAYER2}", args[1]));
        } else {
            sender.sendMessage(fileManager.getMsg("tp.usage"));
        }
        return true;
    }

    private void teleportPlayer(final Player sender, final String playerArg, final BiConsumer<Player, Player> consumer) {
        if (Bukkit.getPlayer(playerArg) == null) {
            final YamlConfiguration playerFile;
            try {
                playerFile = fileManager.getPlayerFile(playerArg);
            } catch (final NullPointerException exception) {
                sender.sendMessage(fileManager.getMsg("cantexist"));
                return;
            }
            final World world = Bukkit.getWorld(playerFile.getString("location.world"));
            final double x = Double.parseDouble(playerFile.getString(".location.x"));
            final double y = Double.parseDouble(playerFile.getString(".location.y"));
            final double z = Double.parseDouble(playerFile.getString(".location.z"));
            final Location location = new Location(world, x, y, z);
            sender.teleport(location);
            sender.sendMessage(fileManager.getMsg("tp.player").replace("{PLAYER}", playerArg));
        } else {
            final Player player = Bukkit.getPlayer(playerArg);
            consumer.accept(sender, player);
        }
    }
}
