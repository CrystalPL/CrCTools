package pl.crystalek.crctools.managers;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MsgManager {
    private final Map<Player, Player> msg = new HashMap<>();
    private final List<Player> socialspy = new ArrayList<>();

    public Player getMsg(final Player player) {
        return msg.get(player);
    }

    public void addMsg(final Player sender, final Player target) {
        msg.put(sender, target);
        msg.put(target, sender);
    }

    public List<Player> getSocialspy() {
        return socialspy;
    }

    public boolean addSpy(final Player player) {
        if (socialspy.contains(player)) {
            socialspy.remove(player);
            return false;
        } else {
            socialspy.add(player);
            return true;
        }
    }
}
