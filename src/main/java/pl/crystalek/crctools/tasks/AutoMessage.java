package pl.crystalek.crctools.tasks;

import org.bukkit.Bukkit;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.ChatUtil;

import java.util.List;

public class AutoMessage {
    int i;

    public AutoMessage(final CrCTools crCTools, final FileManager fileManager) {
        List<String> strings = ChatUtil.fixColor(crCTools.getConfig().getStringList("automessage"));
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(crCTools, () -> {
            Bukkit.broadcastMessage(strings.get(i));
            i++;
            if (i == strings.size()) {
                i = 0;
            }
        }, 0, fileManager.getInt("automessagetime") * 20L);
    }
}
