package pl.crystalek.crctools;

import org.bukkit.plugin.java.JavaPlugin;

public final class CrCTools extends JavaPlugin {
    private ServerStart serverStart;

    @Override
    public void onEnable() {
        serverStart = new ServerStart(this);
    }

    @Override
    public void onDisable() {
        serverStart.savePlayer();
    }
}
