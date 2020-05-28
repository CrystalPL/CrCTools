package pl.crystalek.crctools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public class IpCommand implements CommandExecutor {
    private final FileManager fileManager;

    public IpCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return true;
            }
            sender.sendMessage(fileManager.getMsg("ip.ip").replace("{IP}", ((Player) sender).getAddress().getAddress().getHostAddress()));
        } else if (args.length == 1) {
            if (!sender.hasPermission(fileManager.getPermission("ip.player"))) {
                sender.sendMessage(fileManager.getMsgPermission("ip.player"));
                return true;
            }
            try {
                sender.sendMessage(fileManager.getMsg("ip.player").replace("{PLAYER}", args[0]).replace("{IP}", fileManager.getPlayerFile(args[0]).getString("ip")));
            } catch (NullPointerException exception) {
                sender.sendMessage(fileManager.getMsg("cantexist"));
            }
        }
        return true;
    }
}
