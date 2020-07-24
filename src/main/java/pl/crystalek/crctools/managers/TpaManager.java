package pl.crystalek.crctools.managers;

import pl.crystalek.crctools.exceptions.TeleportingPlayerListEmptyException;
import pl.crystalek.crctools.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaManager {

    public void addTeleport(final User teleportingPlayer, final User playerToTeleport, final boolean option) {
        teleportingPlayer.getTpaList().put(playerToTeleport.getUuid(), option);
    }

    public boolean removeTeleport(final User teleportingPlayer, final User playerToTeleport) {
        if (checkTeleport(teleportingPlayer, playerToTeleport)) {
            teleportingPlayer.getTpaList().remove(playerToTeleport.getUuid());
            return true;
        } else {
            return false;
        }
    }

    public boolean checkTeleport(final User teleportingPlayer, final User playerToTeleport) {
        final boolean b = teleportingPlayer.getTpaList().get(playerToTeleport.getUuid()) != null;
        return b;
    }

    public Map<UUID, Boolean> getPlayerToTp(final User teleporting) throws TeleportingPlayerListEmptyException {
        final Map<UUID, Boolean> tpaList = teleporting.getTpaList();
        if (tpaList.isEmpty()) {
            throw new TeleportingPlayerListEmptyException("no such player");
        } else {
            return new HashMap<>(tpaList);
        }
    }
}
