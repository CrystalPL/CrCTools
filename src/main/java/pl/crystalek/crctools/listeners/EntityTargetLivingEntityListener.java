package pl.crystalek.crctools.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import pl.crystalek.crctools.managers.UserManager;

public final class EntityTargetLivingEntityListener implements Listener {
    private final UserManager userManager;

    public EntityTargetLivingEntityListener(final UserManager userManager) {
        this.userManager = userManager;
    }

    @EventHandler
    public void onDamageEntity(final EntityTargetLivingEntityEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = ((Player) event.getTarget());
            if (userManager.getUser(player).isGod()) {
                event.setCancelled(true);
            }
        }
    }
}
