package pl.crystalek.crctools.tasks;

import org.bukkit.Bukkit;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;

import java.io.IOException;

public final class AutoSave {

    public AutoSave(final CrCTools crCTools, final FileManager fileManager) {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(crCTools, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                try {
                    fileManager.savePlayer(player);
                } catch (final IOException exception) {
                    exception.printStackTrace();
                }
            });
            System.out.println("ZAPIS ZOSTAL ZAKOŃCZONY!");
        }, 200, fileManager.getInt("autosave") * 20L);
    }
}