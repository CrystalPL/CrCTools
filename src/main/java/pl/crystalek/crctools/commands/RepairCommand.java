package pl.crystalek.crctools.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.crystalek.crctools.managers.FileManager;

public final class RepairCommand implements CommandExecutor {
    private final FileManager fileManager;

    final private Material[] repairableMaterials = new Material[]{
            Material.DIAMOND_PICKAXE, Material.DIAMOND_SWORD, Material.DIAMOND_SPADE,
            Material.DIAMOND_AXE, Material.DIAMOND_HOE, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.IRON_PICKAXE,
            Material.IRON_SWORD,
            Material.IRON_SPADE, Material.IRON_AXE, Material.IRON_HOE, Material.IRON_HELMET, Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.GOLD_PICKAXE, Material.GOLD_SWORD,
            Material.GOLD_SPADE, Material.GOLD_AXE, Material.GOLD_HOE, Material.GOLD_HELMET, Material.GOLD_CHESTPLATE,
            Material.GOLD_LEGGINGS, Material.GOLD_BOOTS, Material.STONE_PICKAXE, Material.STONE_SWORD,
            Material.STONE_SPADE,
            Material.STONE_AXE, Material.STONE_HOE, Material.CHAINMAIL_HELMET,
            Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS, Material.WOOD_PICKAXE,
            Material.WOOD_SWORD, Material.WOOD_SPADE, Material.WOOD_AXE,
            Material.WOOD_HOE, Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.FLINT_AND_STEEL,
            Material.SHEARS, Material.BOW, Material.FISHING_ROD};

    public RepairCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (args.length != 0) {
            sender.sendMessage(fileManager.getMsg("repair.usage"));
            return true;
        }
        final Player player = (Player) sender;
        if (!player.hasPermission(fileManager.getPermission("repair.repair"))) {
            player.sendMessage(fileManager.getMsgPermission("repair.repair"));
            return true;
        }
        final ItemStack itemInHand = player.getItemInHand();
        if (!checkItem(itemInHand.getType())) {
            player.sendMessage(fileManager.getMsg("repair.error"));
            return true;
        }
        if (itemInHand.getDurability() == 0) {
            player.sendMessage(fileManager.getMsg("repair.error"));
            return true;
        }
        itemInHand.setDurability((short) 0);
        player.sendMessage(fileManager.getMsg("repair.repair"));
        return true;
    }

    private boolean checkItem(final Material material) {
        for (final Material sought : repairableMaterials) {
            if (sought.equals(material)) {
                return true;
            }
        }
        return false;
    }
}
