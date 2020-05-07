package pl.crystalek.crctools.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialUtil {

    public static ItemStack getMaterial(final String[] args) {
        final String[] idAndData = args[1].split(":");
        Material material = null;
        short data = 0;
        if (args[1].contains(":")) {
            data = Short.parseShort(idAndData[1]);
        }
        try {
            if (Material.matchMaterial(idAndData[0]) != null) {
                material = Material.matchMaterial(idAndData[0]);
            }
        } catch (IllegalArgumentException exception) {
            if (!NumberUtil.isInt(idAndData[0])) {
                return null;
            }
            if (Material.getMaterial(Integer.parseInt(idAndData[0])) != null) {
                material = Material.getMaterial(Integer.parseInt(idAndData[0]));
            }
        }
        return new ItemStack(material, Integer.parseInt(args[2]), data);
    }
}
