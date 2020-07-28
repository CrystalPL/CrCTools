package pl.crystalek.crctools.commands;

import com.destroystokyo.paper.Title;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.ChatUtil;

public final class AlertCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final CrCTools crCTools;

    public AlertCommand(final FileManager fileManager, final CrCTools crCTools) {
        this.fileManager = fileManager;
        this.crCTools = crCTools;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("alert.alert"))) {
            sender.sendMessage(fileManager.getMsgPermission("alert.alert"));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(fileManager.getMsg("alert.usage"));
            return true;
        }
        if (args[0].equalsIgnoreCase("chat")) {
            final String join = StringUtils.join(args, ' ', 1, args.length);
            Bukkit.broadcastMessage(fileManager.getMsg("alert.chat").replace("{MESSAGE}", ChatUtil.fixColor(join)));
        } else if (args[0].equalsIgnoreCase("title")) {
            final String join = StringUtils.join(args, ' ', 1, args.length);
            final Title alert = new Title(fileManager.getMsg("alert.title").replace("{MESSAGE}", ChatUtil.fixColor(join)), "", 20, fileManager.getInt("alerttime") * 20, 20);
            Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(alert));
        } else if (args[0].equalsIgnoreCase("actionbar")) {
            final String join = StringUtils.join(args, ' ', 1, args.length);
            Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(fileManager.getMsg("alert.actionbar").replace("{MESSAGE}", ChatUtil.fixColor(join))));
        } else if (args[0].equalsIgnoreCase("bossbar")) {
            final String join = StringUtils.join(args, ' ', 1, args.length);
            final BossBar bossBar = Bukkit.createBossBar(fileManager.getMsg("alert.bossbar").replace("{MESSAGE}", ChatUtil.fixColor(join)), BarColor.GREEN, BarStyle.SEGMENTED_10);
            Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
            Bukkit.getScheduler().scheduleAsyncDelayedTask(crCTools, () -> Bukkit.getOnlinePlayers().forEach(bossBar::removePlayer), fileManager.getInt("alerttime") * 20L);
        } else {
            sender.sendMessage(fileManager.getMsg("alert.usage"));
        }
        return true;
    }
}
