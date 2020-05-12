package pl.crystalek.crctools.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;

import java.io.IOException;

public class AutoSave {

    public AutoSave(final CrCTools crCTools, final FileManager fileManager) {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(crCTools, () -> {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                try {
                    fileManager.savePlayer(player);
                } catch (IOException ignored) {
                }
            }
            System.out.println("ZAPIS ZOSTAL ZAKOŃCZONY!");
        }, 0, fileManager.getInt("autosave") * 20L);
    }
}