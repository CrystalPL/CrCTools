package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import pl.crystalek.crctools.managers.FileManager;

public final class EnderseeCommand implements CommandExecutor {
    private final FileManager fileManager;

    public EnderseeCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("endersee.endersee"))) {
            sender.sendMessage(fileManager.getMsgPermission("endersee.endersee"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(fileManager.getMsg("endersee.usage"));
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(fileManager.getMsg("offlineplayer"));
            return true;
        }
        final Player playerSender = (Player) sender;
        final Player player = Bukkit.getPlayer(args[0]);
        final Inventory inventory = Bukkit.createInventory(playerSender, InventoryType.PLAYER, fileManager.getMsg("endersee.title").replace("{PLAYER}", player.getName()));
        final Inventory playerInventory = player.getEnderChest();
        for (int i = 0; i < playerInventory.getSize(); i++) {
            inventory.setItem(i, playerInventory.getItem(i));
        }
        playerSender.openInventory(inventory);
        return true;
    }
}
//try {
//        Field a = EntityHuman.class.getDeclaredField("enderChest");
//        a.setAccessible(true);
//        inventoryEnderChest enderChest = (InventoryEnderChest) a.get((EntityHuman) ((CraftPlayer) player).getHandle());
//        Field chestA = InventorySubcontainer.class.getDeclaredField("a");
//        chestA.setAccessible(true);
//        chestA.set(enderChest, ChatUtil.color("&6Ender Chest Kek"));
//        } catch(Exception ex){
//        ex.printStackTrace();
//        }
