package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.NumberUtil;

public class GamemodeCommand implements CommandExecutor {
    private final FileManager fileManager;

    public GamemodeCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(args.length >= 1 && args.length <= 2)) {
            sender.sendMessage(fileManager.getMsg("gamemode.usage"));
            return true;
        }
        if (getGamemode(args[0]) == null) {
            sender.sendMessage(fileManager.getMsg("gamemode.usage"));
            return true;
        }
        final GameMode gamemode = getGamemode(args[0]);
        if (args.length == 2) {
            if (!sender.hasPermission(fileManager.getPermission("gamemode.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("gamemode.player"));
                return true;
            }
            if (Bukkit.getPlayer(args[1]) == null) {
                sender.sendMessage(fileManager.getMsg("offlineplayer"));
                return true;
            }
            final Player target = Bukkit.getPlayer(args[1]);
            target.setGameMode(gamemode);
            sender.sendMessage(fileManager.getMsg("gamemode.sender").replace("{PLAYER}", target.getName()).replace("{GAMEMODE}", getGamemode(args[0]).toString()));
            target.sendMessage(fileManager.getMsg("gamemode.player").replace("{GAMEMODE}", gamemode.toString()));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        final Player player = (Player) sender;
        if (!player.hasPermission(fileManager.getPermission("gamemode.gamemode"))) {
            player.sendMessage(fileManager.getMsgPermission("gamemode.gamemode"));
            return true;
        }
        player.setGameMode(gamemode);
        player.sendMessage(fileManager.getMsg("gamemode.player").replace("{GAMEMODE}", gamemode.toString()));
        return true;
    }

    private GameMode getGamemode(final String gamemode) {
        try {
            if (GameMode.valueOf(gamemode.toUpperCase()) != null) {
                return GameMode.valueOf(gamemode.toUpperCase());
            }
        } catch (IllegalArgumentException exception) {
            if (!NumberUtil.isInt(gamemode)) {
                return null;
            }
            if (GameMode.getByValue(Integer.parseInt(gamemode)) != null) {
                return GameMode.getByValue(Integer.parseInt(gamemode));
            }
        }
        return null;
    }
}