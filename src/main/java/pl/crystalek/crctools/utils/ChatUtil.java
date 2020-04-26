package pl.crystalek.crctools.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ChatUtil {

    public static String fixColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> fixColor(List<String> list) {
        List<String> lists = new ArrayList<>();
        for (String string : list) {
            lists.add(fixColor(string));
        }
        return lists;
    }
}
