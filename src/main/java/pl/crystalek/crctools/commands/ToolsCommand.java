package pl.crystalek.crctools.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.ChatUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ToolsCommand implements CommandExecutor {
    private final FileManager fileManager;

    public ToolsCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("tools.tools"))) {
            sender.sendMessage(fileManager.getMsgPermission("tools.tools"));
            return true;
        }
        if (!(args.length >= 1)) {
            sender.sendMessage(fileManager.getMsg("tools.usage"));
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (args.length == 1) {
                fileManager.checkFiles();
                sender.sendMessage(fileManager.getMsg("tools.succsess"));
            } else {
                sender.sendMessage(fileManager.getMsg("tools.usage"));
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return true;
            }
            if (args.length != 2) {
                sender.sendMessage(fileManager.getMsg("tools.usage"));
                return true;
            }
            final ConfigurationSection configurationSection = fileManager.getConfigurationSection(args[1].toLowerCase());
            if (configurationSection == null) {
                sender.sendMessage(fileManager.getMsg("tools.errorkey"));
                return true;
            }
            final List<String> keyList = new ArrayList<>(configurationSection.getKeys(true));
            final List<Object> valueList = new ArrayList<>(configurationSection.getValues(true).values());

            sender.sendMessage(fileManager.getMsg("tools.startlist").replace("{PATH}", args[1]));
            for (int i = 0; i < keyList.size(); i++) {
                if (valueList.get(i) instanceof ArrayList) {
                    for (final Object o : (ArrayList) valueList.get(i)) {
                        ChatUtil.sendHoverMessage((Player) sender, " - " + o, fileManager.getMsg("tools.list").replace("{HOVER}", keyList.get(i)));
                    }
                } else {
                    ChatUtil.sendHoverMessage((Player) sender, (String) valueList.get(i), fileManager.getMsg("tools.list").replace("{HOVER}", keyList.get(i)));
                }
            }
            sender.sendMessage(fileManager.getMsg("tools.startlist").replace("{PATH}", args[1]));
        } else if (args[0].equalsIgnoreCase("set")) {
            if (!(args.length >= 3)) {
                sender.sendMessage(fileManager.getMsg("tools.usage"));
                return true;
            }
            final String join = StringUtils.join(args, ' ', 2, args.length);
            try {
                if (fileManager.setValuePath(args[1], join)) {
                    sender.sendMessage(fileManager.getMsg("tools.success"));
                } else {
                    sender.sendMessage(fileManager.getMsg("tools.errorkey"));
                }
            } catch (IOException e) {
                sender.sendMessage(fileManager.getMsg("tools.usage"));
            }
        } else {
            sender.sendMessage(fileManager.getMsg("tools.usage"));
        }
        return true;
    }
}