package pl.crystalek.crctools.managers;

import org.bukkit.entity.Player;
import pl.crystalek.crctools.model.User;

import java.util.HashMap;
import java.util.UUID;

public class UserManager {
    private HashMap<UUID, User> users = new HashMap<>();

    public void addUser(final Player player) {
        users.put(player.getUniqueId(), new User(player));
    }

    public void addUser(final Player player, final UUID uuid, final String lastName, final String ip, final boolean msg, final boolean tpa) {
        users.put(player.getUniqueId(), new User(uuid, lastName, ip, msg, tpa));
    }

    public void removeUser(final Player player) {
        users.remove(player.getUniqueId());
    }

    public User getUser(final Player player) {
        return users.get(player.getUniqueId());
    }

}
