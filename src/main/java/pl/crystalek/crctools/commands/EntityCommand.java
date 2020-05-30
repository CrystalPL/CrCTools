package pl.crystalek.crctools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;

public class EntityCommand implements CommandExecutor {
    private final FileManager fileManager;

    public EntityCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        final Player player = (Player) sender;
        if (!player.hasPermission(fileManager.getPermission("entity.clear"))) {
            player.sendMessage(fileManager.getMsgPermission("entity.clear"));
            return true;
        }
        if (args.length != 2 || !args[0].equalsIgnoreCase("clear")) {
            player.sendMessage(fileManager.getMsg("entity.usage"));
            return true;
        }

        int counter = 0;
        for (final Entity entity : player.getWorld().getEntities()) {
            if (!(entity instanceof Player)) {
                if (args[1].equalsIgnoreCase("mobs")) {
                    if (entity instanceof LivingEntity) {
                        entity.remove();
                        counter++;
                    }
                } else if (args[1].equalsIgnoreCase("items")) {
                    if (entity instanceof Item) {
                        entity.remove();
                        counter++;
                    }
                } else {
                    player.sendMessage(fileManager.getMsg("entity.usage"));
                    return true;
                }
            }
        }
        player.sendMessage(fileManager.getMsg("entity.entity").replace("{NUMBER}", String.valueOf(counter)));
        return true;
    }
}
