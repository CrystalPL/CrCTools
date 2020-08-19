package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.ServerOptions;
import pl.crystalek.crctools.managers.FileManager;

public final class ChatCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final CrCTools crCTools;

    public ChatCommand(final FileManager fileManager, final CrCTools crCTools) {
        this.fileManager = fileManager;
        this.crCTools = crCTools;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("chat.chat"))) {
            sender.sendMessage(fileManager.getMsgPermission("chat.chat"));
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(fileManager.getMsg("chat.usage"));
            return true;
        }
        final FileConfiguration config = crCTools.getConfig();
        if (args[0].equalsIgnoreCase("clear")) {
            clearChat();
            printMessage(sender, "chat.clear");
        } else if (args[0].equalsIgnoreCase("off")) {
            if (!ServerOptions.isChat()) {
                sender.sendMessage(fileManager.getMsg("chat.erroroff"));
                return true;
            }
            config.set("chat", false);
            ServerOptions.setChat(false);
            printMessage(sender, "chat.chatoff");
        } else if (args[0].equalsIgnoreCase("on")) {
            if (ServerOptions.isChat()) {
                sender.sendMessage(fileManager.getMsg("chat.erroron"));
                return true;
            }
            config.set("chat", true);
            ServerOptions.setChat(true);
            printMessage(sender, "chat.chaton");
        } else if (args[0].equalsIgnoreCase("offc")) {
            if (!ServerOptions.isChat()) {
                sender.sendMessage(fileManager.getMsg("chat.erroroff"));
                return true;
            }

            config.set("chat", false);
            ServerOptions.setChat(false);
            clearChat();
            printMessage(sender, "chat.chatoff");
        } else if (args[0].equalsIgnoreCase("onc")) {
            if (ServerOptions.isChat()) {
                sender.sendMessage(fileManager.getMsg("chat.erroron"));
                return true;
            }
            config.set("chat", true);
            ServerOptions.setChat(true);
            clearChat();
            printMessage(sender, "chat.chaton");
        } else {
            sender.sendMessage(fileManager.getMsg("chat.usage"));
        }
        crCTools.saveConfig();
        return true;
    }

    private void printMessage(final CommandSender sender, final String string) {
        for (final String message : fileManager.getMsgList(string)) {
            Bukkit.broadcastMessage(message.replace("{PLAYER}", sender.getName()));
        }
    }

    private void clearChat() {
        for (int i = 0; i < 99; i++) {
            Bukkit.broadcastMessage("");
        }
    }
}