package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.UserManager;
import pl.crystalek.crctools.model.User;

public class GodCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final UserManager userManager;

    public GodCommand(final FileManager fileManager, final UserManager userManager) {
        this.fileManager = fileManager;
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 1) {
            if (!sender.hasPermission(fileManager.getPermission("god.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("god.player"));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(fileManager.getMsg("offlineplayer"));
                return true;
            }
            final Player player = Bukkit.getPlayer(args[0]);
            final User user = userManager.getUser(player);
            if (user.isGod()) {
                user.setGod(false);
                player.sendMessage(fileManager.getMsg("god.godoff"));
                sender.sendMessage(fileManager.getMsg("god.playeroff").replace("{PLAYER}", player.getName()));
            } else {
                user.setGod(true);
                player.sendMessage(fileManager.getMsg("god.godon"));
                sender.sendMessage(fileManager.getMsg("god.playeron").replace("{PLAYER}", player.getName()));
            }
        } else if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return true;
            }
            if (!sender.hasPermission(fileManager.getPermission("god.god"))) {
                sender.sendMessage(fileManager.getMsgPermission("god.god"));
                return true;
            }
            final Player player = (Player) sender;
            final User user = userManager.getUser(player);
            if (user.isGod()) {
                user.setGod(false);
                player.sendMessage(fileManager.getMsg("god.godoff"));
            } else {
                user.setGod(true);
                player.sendMessage(fileManager.getMsg("god.godon"));
            }
        } else {
            sender.sendMessage(fileManager.getMsg("god.usage"));
        }
        return true;
    }
}