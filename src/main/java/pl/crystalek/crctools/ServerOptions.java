package pl.crystalek.crctools;

import org.bukkit.Location;

public class ServerOptions {
    private static Location spawnLocation;
    private static boolean chat;

    public ServerOptions(final Location spawnLocation, final boolean chat) {
        ServerOptions.spawnLocation = spawnLocation;
        ServerOptions.chat = chat;
    }

    public static Location getSpawnLocation() {
        return spawnLocation;
    }

    public static void setSpawnLocation(final Location spawnLocation) {
        ServerOptions.spawnLocation = spawnLocation;
    }

    public static boolean isChat() {
        return chat;
    }

    public static void setChat(final boolean chat) {
        ServerOptions.chat = chat;
    }
}
