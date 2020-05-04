package pl.crystalek.crctools.utils;

import org.bukkit.entity.Player;
import pl.crystalek.crctools.exceptions.TeleportingPlayerNotExist;

import java.util.*;

public class TpaUtil {
    private Map<UUID, List<Player>> tpaList = new HashMap<>();

    public void addTeleport(UUID teleporting, Player user) {
        if (tpaList.containsKey(teleporting)) {
            tpaList.get(teleporting).add(user);
        } else {
            List<Player> playerList = new ArrayList<>(1);
            playerList.add(user);
            tpaList.put(teleporting, playerList);
        }
    }

    public boolean removeTeleport(UUID teleporting, Player user) {
        if (checkTeleport(teleporting, user)) {
            return tpaList.get(teleporting).remove(user);
        } else {
            return false;
        }
    }

    public boolean checkTeleport(UUID teleporting, Player user) {
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

    public List<Player> getPlayerToTp(UUID teleporting) throws TeleportingPlayerNotExist {
        if (tpaList.get(teleporting) == null) {
            throw new TeleportingPlayerNotExist("no such player");
        } else if (tpaList.get(teleporting).isEmpty()) {
            tpaList.remove(teleporting);
            throw new TeleportingPlayerNotExist("no such player");
        } else {
            return new ArrayList<>(tpaList.get(teleporting));
        }
    }
}
