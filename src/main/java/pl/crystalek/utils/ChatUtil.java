package pl.crystalek.utils;

import org.bukkit.ChatColor;

public class ChatUtil {

    public static String fixColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
