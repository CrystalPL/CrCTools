package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.NumberUtil;

public final class ReloadCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final CrCTools crCTools;

    public ReloadCommand(final FileManager fileManager, final CrCTools crCTools) {
        this.fileManager = fileManager;
        this.crCTools = crCTools;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("reload.reload"))) {
            sender.sendMessage(fileManager.getMsgPermission("reload.reload"));
            return true;
        }
        final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        if (args.length == 0) {
            Bukkit.dispatchCommand(console, "rl confirm");
            sender.sendMessage(fileManager.getMsg("reload.reload"));
        } else if (args.length == 1) {
            if (!NumberUtil.isInt(args[0])) {
                sender.sendMessage(fileManager.getMsg("reload.usage"));
                return true;
            }
            final int time = Integer.parseInt(args[0]);
            new BukkitRunnable() {
                private int i;

                public void run() {
                    Bukkit.broadcastMessage(fileManager.getMsg("reload.broadcast").replace("{TIME}", String.valueOf(time - i)));
                    i++;
                    if (i == time) {
                        cancel();
                        Bukkit.dispatchCommand(console, "rl confirm");
                        sender.sendMessage(fileManager.getMsg("reload.reload"));
                    }
                }
            }.runTaskTimer(crCTools, 0L, 20L);
        } else {
            sender.sendMessage(fileManager.getMsg("reload.usage"));
        }
        return true;
    }
}
