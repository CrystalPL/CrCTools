package pl.crystalek.crctools.utils;

import me.idlibrary.main.IDLibrary;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.crystalek.crctools.exceptions.NotDetectedItemException;

public final class MaterialUtil {
    private final static IDLibrary idLibrary = (IDLibrary) Bukkit.getPluginManager().getPlugin("ID-Library");

    public static ItemStack getMaterial(final String[] args, int argument, final Inventory inventory, final Short amountItemArgument) throws NotDetectedItemException {
        final String[] idAndData = args[argument].split(":");
        short data = 0;
        if (args[argument].contains(":")) {
            data = Short.parseShort(idAndData[1]);
        }
        Material material = null;
        if (idLibrary.getMaterial(idAndData[0]) != null) {
            material = idLibrary.getMaterial(idAndData[0]);
        }
        checkAvailableSpace(inventory, amountItemArgument, material);
        return new ItemStack(material, amountItemArgument, data);
    }

    public static ItemStack getMaterialHand(final Inventory inventory, final ItemStack itemInHand, final short amountItemArgument) throws NotDetectedItemException {
        final ItemStack itemStack = new ItemStack(itemInHand);
        checkAvailableSpace(inventory, amountItemArgument, itemStack.getType());
        itemStack.setAmount(amountItemArgument);
        return itemStack;
    }

    private static void checkAvailableSpace(final Inventory inventory, final Short amountItemArgument, final Material material) {
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
        if (!(amountItemArgument <= (2304 - amountItem))) {
            throw new NotDetectedItemException("full eq");
        }
    }
}