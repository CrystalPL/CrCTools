package pl.crystalek.crctools.model;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgManager {
    private Map<Player, Player> msg = new HashMap<>();
    private List<Player> socialspy = new ArrayList<>();

    public Player getMsg(Player player) {
        return msg.get(player);
    }

    public void addMsg(Player sender, Player target) {
        msg.put(sender, target);
        msg.put(target, sender);
    }

    public List<Player> getSocialspy() {
        return socialspy;
    }

    public boolean addSpy(Player player) {
        if (socialspy.contains(player)) {
            socialspy.remove(player);
            return false;
        } else {
            socialspy.add(player);
            return true;
        }
    }
}
