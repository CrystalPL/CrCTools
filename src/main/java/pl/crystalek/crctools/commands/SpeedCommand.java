package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.NumberUtil;

public final class SpeedCommand implements CommandExecutor {
    private final FileManager fileManager;

    public SpeedCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(args.length >= 1 && args.length <= 2)) {
            sender.sendMessage(fileManager.getMsg("speed.usage"));
            return true;
        }
        if (!NumberUtil.isInt(args[0])) {
            sender.sendMessage(fileManager.getMsg("speed.errornumber"));
            return true;
        }
        if (!(Integer.parseInt(args[0]) >= 0 && Integer.parseInt(args[0]) <= 10)) {
            sender.sendMessage(fileManager.getMsg("speed.error"));
            return true;
        }
        final float speed = Integer.parseInt(args[0]) / 10F;
        if (args.length == 2) {
            if (!sender.hasPermission(fileManager.getPermission("speed.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("speed.player"));
                return true;
            }
            if (Bukkit.getPlayer(args[1]) == null) {
                sender.sendMessage(fileManager.getMsg("offlineplayer"));
                return true;
            }
            final Player player = Bukkit.getPlayer(args[1]);
            if (player.isFlying()) {
                player.setFlySpeed(speed);
                player.sendMessage(fileManager.getMsg("speed.fly").replace("{SPEED}", args[0]));
                sender.sendMessage(fileManager.getMsg("speed.flyplayer").replace("{SPEED}", args[0]).replace("{PLAYER}", player.getName()));
            } else {
                player.setWalkSpeed(speed);
                player.sendMessage(fileManager.getMsg("speed.walk").replace("{SPEED}", args[0]));
                sender.sendMessage(fileManager.getMsg("speed.walkplayer").replace("{SPEED}", args[0]).replace("{PLAYER}", player.getName()));
            }
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (!sender.hasPermission(fileManager.getPermission("speed.speed"))) {
            sender.sendMessage(fileManager.getMsgPermission("speed.speed"));
            return true;
        }
        final Player player = (Player) sender;
        if (player.isFlying()) {
            player.setFlySpeed(speed);
            player.sendMessage(fileManager.getMsg("speed.fly").replace("{SPEED}", args[0]));
        } else {
            player.setWalkSpeed(speed);
            player.sendMessage(fileManager.getMsg("speed.walk").replace("{SPEED}", args[0]));
        }

        return true;
    }
}