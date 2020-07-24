package pl.crystalek.crctools.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.ChatUtil;

import java.util.Arrays;
import java.util.List;

public class LoreCommand implements CommandExecutor {
    private final FileManager fileManager;

    public LoreCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("lore.lore"))) {
            sender.sendMessage(fileManager.getMsgPermission("lore.lore"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(fileManager.getMsg("lore.usage"));
            return true;
        }
        final String join = StringUtils.join(args, ' ', 0, args.length);
        final List<String> strings = ChatUtil.fixColor(Arrays.asList(StringUtils.split(join, "||")));
        final Player player = (Player) sender;
        final ItemStack itemInHand = player.getItemInHand();
        final ItemMeta meta = itemInHand.getItemMeta();
        meta.setLore(strings);
        itemInHand.setItemMeta(meta);
        sender.sendMessage(fileManager.getMsg("lore.lore"));
        strings.forEach(sender::sendMessage);
        return true;
    }
}
