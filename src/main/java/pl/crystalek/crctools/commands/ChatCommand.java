package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.crystalek.crctools.managers.FileManager;

public class ChatCommand implements CommandExecutor {
    private final FileManager fileManager;
    public static boolean CHAT = true;

    public ChatCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
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

        if (args[0].equalsIgnoreCase("clear")) {
            clearChat();
            printMessage(sender, "chat.clear");
        } else if (args[0].equalsIgnoreCase("off")) {
            if (!CHAT) {
                sender.sendMessage(fileManager.getMsg("chat.erroroff"));
                return true;
            }
            CHAT = false;
            printMessage(sender, "chat.chatoff");
        } else if (args[0].equalsIgnoreCase("on")) {
            if (CHAT) {
                sender.sendMessage(fileManager.getMsg("chat.erroron"));
                return true;
            }
            CHAT = true;
            printMessage(sender, "chat.chaton");
        } else if (args[0].equalsIgnoreCase("offc")) {
            if (!CHAT) {
                sender.sendMessage(fileManager.getMsg("chat.erroroff"));
                return true;
            }
            CHAT = false;
            clearChat();
            printMessage(sender, "chat.chatoff");
        } else if (args[0].equalsIgnoreCase("onc")) {
            if (CHAT) {
                sender.sendMessage(fileManager.getMsg("chat.erroron"));
                return true;
            }
            CHAT = true;
            clearChat();
            printMessage(sender, "chat.chaton");
        } else {
            sender.sendMessage(fileManager.getMsg("chat.usage"));
        }
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