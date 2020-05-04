package pl.crystalek.crctools.managers;

import org.bukkit.entity.Player;
import pl.crystalek.crctools.exceptions.TeleportingPlayerListEmpty;

import java.util.*;

public class TpaManager {
    private Map<UUID, List<Player>> tpaList = new HashMap<>();

    public void addTeleport(final UUID teleporting, final Player user) {
        if (tpaList.containsKey(teleporting)) {
            tpaList.get(teleporting).add(user);
        } else {
            final List<Player> playerList = new ArrayList<>(1);
            playerList.add(user);
            tpaList.put(teleporting, playerList);
        }
    }

    public boolean removeTeleport(final UUID teleporting, final Player user) {
        if (checkTeleport(teleporting, user)) {
            return tpaList.get(teleporting).remove(user);
        } else {
            return false;
        }
    }

    public boolean checkTeleport(final UUID teleporting, final Player user) {
        if (tpaList.get(teleporting) == null) {
            return false;
        }
        if (tpaList.get(teleporting).isEmpty()) {
            tpaList.remove(teleporting);
            return false;
        } else {
            return tpaList.get(teleporting).contains(user);
        }
    }

    public List<Player> getPlayerToTp(final UUID teleporting) throws TeleportingPlayerListEmpty {
        if (tpaList.get(teleporting) == null) {
            throw new TeleportingPlayerListEmpty("no such player");
        } else if (tpaList.get(teleporting).isEmpty()) {
            tpaList.remove(teleporting);
            throw new TeleportingPlayerListEmpty("no such player");
        } else {
            return new ArrayList<>(tpaList.get(teleporting));
        }
    }
}
