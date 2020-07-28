package pl.crystalek.crctools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public final class SuicideCommand implements CommandExecutor {
    private final FileManager fileManager;

    public SuicideCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length != 0) {
            sender.sendMessage(fileManager.getMsg("suicide.usage"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        ((Player) sender).setHealth(0);
        sender.sendMessage(fileManager.getMsg("suicide.suicide"));
        return true;
    }
}
