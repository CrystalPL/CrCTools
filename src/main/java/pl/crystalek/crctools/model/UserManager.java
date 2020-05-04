package pl.crystalek.crctools.model;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class UserManager {
    private HashMap<UUID, User> users = new HashMap<>();

    public void addUser(Player player) {
        users.put(player.getUniqueId(), new User(player));
    }

    public void addUser(Player player, boolean msg, boolean tpa) {
        users.put(player.getUniqueId(), new User(player, msg, tpa));
    }

    public void removeUser(Player player) {
        users.remove(player.getUniqueId());
    }

    public User getUser(Player player) {
        return users.get(player.getUniqueId());
    }

}
