package pl.crystalek.crctools.utils;

import me.idlibrary.main.IDLibrary;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.crystalek.crctools.exceptions.NotDetectedItemException;

public class MaterialUtil {

    public static ItemStack getMaterial(final String[] args, int argument, final Inventory inventory) throws NotDetectedItemException {
        final String[] idAndData = args[argument].split(":");
        short data = 0;
        if (args[argument].contains(":")) {
            data = Short.parseShort(idAndData[1]);
        }
        Material material = null;
        if (IDLibrary.getMaterial(idAndData[0]) != null) {
            material = IDLibrary.getMaterial(idAndData[0]);
        }
        short amountItem = 0;
        for (int i = 0; i < 36; i++) {
            final ItemStack item = inventory.getItem(i);
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
            throw new NotDetectedItemException("full eq");
        }
        if (!(Short.parseShort(args[argument + 1]) <= (2304 - amountItem))) {
            throw new NotDetectedItemException("full eq");
        }
        return new ItemStack(material, Integer.parseInt(args[argument + 1]), data);
    }
}