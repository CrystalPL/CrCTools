package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.UserManager;
import pl.crystalek.crctools.model.User;

import java.text.DecimalFormat;
import java.util.List;

public class WhoisCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final UserManager userManager;
    private final DecimalFormat decimalFormat;

    public WhoisCommand(final FileManager fileManager, final UserManager userManager, final DecimalFormat decimalFormat) {
        this.fileManager = fileManager;
        this.userManager = userManager;
        this.decimalFormat = decimalFormat;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("whois.whois"))) {
            sender.sendMessage(fileManager.getMsgPermission("whois.whois"));
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(fileManager.getMsg("whois.usage"));
            return true;
        }
        final List<String> msgList = fileManager.getMsgList("whois.whois");
        if (Bukkit.getPlayer(args[0]) != null) {
            final Player player = Bukkit.getPlayer(args[0]);
            final User user = userManager.getUser(player);
            final Location location = player.getLocation();
            for (final String msg : msgList) {
                sender.sendMessage(msg
                        .replace("{PLAYER}", player.getName())
                        .replace("{BAN}", String.valueOf(player.isBanned()))
                        .replace("{LEVEL}", String.valueOf(player.getLevel()))
                        .replace("{FLY}", String.valueOf(player.getAllowFlight()))
                        .replace("{GAMEMODE}", player.getGameMode().name())
                        .replace("{WORLD}", location.getWorld().getName())
                        .replace("{X}", decimalFormat.format(location.getX()))
                        .replace("{Y}", decimalFormat.format(location.getY()))
                        .replace("{Z}", decimalFormat.format(location.getZ()))
                        .replace("{GOD}", String.valueOf(user.isGod()))
                        .replace("{HEALTH}", String.valueOf((int) player.getHealth()))
                        .replace("{GLOD}", String.valueOf(player.getFoodLevel()))
                        .replace("{OP}", String.valueOf(player.isOp()))
                        .replace("{IP}", player.getAddress().getAddress().getHostAddress())
                        .replace("{UUID}", user.getUuid().toString()));
            }
        } else {
            YamlConfiguration playerFile;
            try {
                playerFile = fileManager.getPlayerFile(args[0]);
            } catch (NullPointerException exception) {
                sender.sendMessage(fileManager.getMsg("cantexist"));
                return true;
            }
            final OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
            for (final String msg : msgList) {
                sender.sendMessage(msg
                        .replace("{PLAYER}", playerFile.getString("nick"))
                        .replace("{BAN}", String.valueOf(player.isBanned()))
                        .replace("{LEVEL}", String.valueOf(playerFile.getInt("food")))
                        .replace("{FLY}", "false")
                        .replace("{GAMEMODE}", playerFile.getString("gamemode"))
                        .replace("{WORLD}", String.valueOf(playerFile.getString("location.world")))
                        .replace("{X}", playerFile.getString("location.x"))
                        .replace("{Y}", playerFile.getString("location.y"))
                        .replace("{Z}", playerFile.getString("location.z"))
                        .replace("{GOD}", String.valueOf(playerFile.getBoolean("god")))
                        .replace("{HEALTH}", String.valueOf(playerFile.getInt("health")))
                        .replace("{GLOD}", playerFile.getString("food"))
                        .replace("{OP}", String.valueOf(player.isOp()))
                        .replace("{IP}", playerFile.getString("ip"))
                        .replace("{UUID}", playerFile.getString("uuid")));
            }
        }
        return true;
    }
}