package pl.crystalek.crctools.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public final class WeatherChangeListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWeatherChange(final WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }
}
