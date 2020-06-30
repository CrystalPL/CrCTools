package pl.crystalek.crctools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;

public class RankCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final CrCTools crCTools;

    public RankCommand(final FileManager fileManager, CrCTools crCTools) {
        this.fileManager = fileManager;
        this.crCTools = crCTools;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
        }
        return true;
    }
}
