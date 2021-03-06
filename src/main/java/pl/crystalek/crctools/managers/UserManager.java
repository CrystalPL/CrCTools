package pl.crystalek.crctools.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class UserManager {
    private final CrCTools crCTools;
    private final Map<UUID, User> users = new HashMap<>();

    public UserManager(final CrCTools crCTools) {
        this.crCTools = crCTools;
    }

    public void addUser(final Player player) {
        users.put(player.getUniqueId(), new User(player, crCTools));
    }

    public void addUser(final Player player, final UUID uuid, final String ip, final boolean msg, final boolean tpa, final boolean god, final String displayName, final String nickColor, final String messageColor, final Map<String, Location> homeList, final List<String> groupList) {
        users.put(player.getUniqueId(), new User(uuid, ip, msg, tpa, god, displayName, nickColor, messageColor, homeList, crCTools, groupList));
    }

    public void addUser(final Player player, final UUID uuid, final String ip, final boolean msg, final boolean tpa, final boolean god, final String displayName, final String nickColor, final String messageColor, final List<String> groupList) {
        users.put(player.getUniqueId(), new User(uuid, ip, msg, tpa, god, crCTools, displayName, nickColor, messageColor, groupList));
    }

    public void removeUser(final Player player) {
        users.remove(player.getUniqueId());
    }

    public User getUser(final Player player) {
        return users.get(player.getUniqueId());
    }
}