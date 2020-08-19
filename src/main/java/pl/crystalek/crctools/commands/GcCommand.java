package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.crystalek.crctools.managers.FileManager;

import java.text.DecimalFormat;
import java.util.List;

public class GcCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final DecimalFormat decimalFormat;

    public GcCommand(final FileManager fileManager, final DecimalFormat decimalFormat) {
        this.fileManager = fileManager;
        this.decimalFormat = decimalFormat;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("gc.gc"))) {
            sender.sendMessage(fileManager.getMsgPermission("gc.gc"));
            return true;
        }
        if (args.length != 0) {
            sender.sendMessage(fileManager.getMsg("gc.usage"));
            return true;
        }
        final List<String> gcListMessage = fileManager.getMsgList("gc.gc");
        final List<String> worldListMessage = fileManager.getMsgList("gc.world");
        final Runtime runtime = Runtime.getRuntime();
        for (final String message : gcListMessage) {
            sender.sendMessage(message
                    .replace("{TPS}", String.valueOf(decimalFormat.format(Bukkit.getTPS()[0])))
                    .replace("{CORES}", String.valueOf(runtime.availableProcessors() / 2))
                    .replace("{THREADS}", String.valueOf(runtime.availableProcessors()))
                    .replace("{MEMORY}", String.valueOf(runtime.maxMemory() / 1024L / 1024L))
                    .replace("{FREEMEMORY}", String.valueOf(runtime.freeMemory() / 1024L / 1024L))
                    .replace("{TOTALMEMORY}", String.valueOf(runtime.totalMemory() / 1024L / 1024L)));
        }
        for (final World world : Bukkit.getWorlds()) {
            for (final String message : worldListMessage) {
                sender.sendMessage(message
                        .replace("{WORLD}", world.getName())
                        .replace("{ENTITES}", String.valueOf(world.getEntities().size()))
                        .replace("{CHUNKS}", String.valueOf(world.getLoadedChunks().length))
                        .replace("{LIVEENTITES}", String.valueOf(world.getLivingEntities().size())));
            }
        }
        return true;
    }
}
