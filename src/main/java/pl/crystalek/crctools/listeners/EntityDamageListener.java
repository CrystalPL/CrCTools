package pl.crystalek.crctools.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import pl.crystalek.crctools.managers.UserManager;

public class EntityDamageListener implements Listener {
    private final UserManager userManager;

    public EntityDamageListener(final UserManager userManager) {
        this.userManager = userManager;
    }

    @EventHandler
    public void onDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = ((Player) event.getEntity()).getPlayer();
            if (userManager.getUser(player).isGod()) {
                event.setCancelled(true);
            }
        }
    }
}
