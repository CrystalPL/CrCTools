package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import pl.crystalek.crctools.managers.FileManager;

public final class InvseeCommand implements CommandExecutor {
    private final FileManager fileManager;

    public InvseeCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("invsee.invsee"))) {
            sender.sendMessage(fileManager.getMsgPermission("invsee.invsee"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(fileManager.getMsg("invsee.usage"));
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(fileManager.getMsg("offlineplayer"));
            return true;
        }
        final Player playerSender = (Player) sender;
        final Player player = Bukkit.getPlayer(args[0]);
        final Inventory inventory = Bukkit.createInventory(playerSender, InventoryType.PLAYER, fileManager.getMsg("invsee.title").replace("{PLAYER}", player.getName()));
        final PlayerInventory playerInventory = player.getInventory();
        for (int i = 0; i < playerInventory.getSize(); i++) {
            inventory.setItem(i, playerInventory.getItem(i));
        }
        playerSender.openInventory(inventory);
        return true;
    }
}
