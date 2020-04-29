package pl.crystalek.crctools.model;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class UserManager {
    private static HashMap<UUID, User> users = new HashMap<>();

    public void addUser(Player player) {
        users.put(player.getUniqueId(), new User(player));
    }

    public void removeUser(Player player) {
        users.remove(player.getUniqueId(), new User(player));
    }

    public User getUser(UUID uuid) {
        return users.get(uuid);
    }
}
