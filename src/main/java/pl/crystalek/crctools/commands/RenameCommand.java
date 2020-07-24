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

public class RenameCommand implements CommandExecutor {
    private final FileManager fileManager;

    public RenameCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("rename.rename"))) {
            sender.sendMessage(fileManager.getMsgPermission("rename.rename"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(fileManager.getMsg("rename.usage"));
            return true;
        }
        final String text = StringUtils.join(args, ' ', 0, args.length);
        final Player player = (Player) sender;
        final ItemStack itemInHand = player.getItemInHand();
        final ItemMeta meta = itemInHand.getItemMeta();
        meta.setDisplayName(ChatUtil.fixColor(text));
        itemInHand.setItemMeta(meta);
        sender.sendMessage(fileManager.getMsg("rename.rename").replace("{NAME}", ChatUtil.fixColor(text)));
        return true;
    }
}
