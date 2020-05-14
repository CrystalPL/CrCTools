package pl.crystalek.crctools.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MaterialUtil {

    public static ItemStack getMaterial(final String[] args, int argument, final Inventory inventory) {
        final String[] idAndData = args[argument].split(":");
        short data = 0;
        if (args[argument].contains(":")) {
            data = Short.parseShort(idAndData[1]);
        }
        Material material = null;
        if (Material.matchMaterial(idAndData[0]) != null) {
            material = Material.matchMaterial(idAndData[0]);
        }
        short amountItem = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) {
                continue;
            }
            if (item.getType().equals(material)) {
                amountItem += item.getAmount();
            } else {
                amountItem += 64;
            }
        }
        if (amountItem == 2304) {
            throw new IllegalArgumentException("full eq");
        }
        if (!(Short.parseShort(args[argument + 1]) <= (2304 - amountItem))) {
            throw new IllegalArgumentException("full eq");
        }
        return new ItemStack(material, Integer.parseInt(args[argument + 1]), data);
    }
}