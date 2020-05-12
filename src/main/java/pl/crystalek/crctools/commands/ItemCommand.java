package pl.crystalek.crctools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.MaterialUtil;
import pl.crystalek.crctools.utils.NumberUtil;

public class ItemCommand implements CommandExecutor {
    private final FileManager fileManager;

    public ItemCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        final Player player = (Player) sender;
        if (!player.hasPermission(fileManager.getPermission("item.item"))) {
            player.sendMessage(fileManager.getMsgPermission("item.item"));
            return true;
        }
        if (args.length != 2) {
            player.sendMessage(fileManager.getMsg("item.usage"));
            return true;
        }
        if (!NumberUtil.isInt(args[1])) {
            player.sendMessage(fileManager.getMsg("speed.errornumber"));
            return true;
        }
        final PlayerInventory inventory = player.getInventory();
        if (inventory.firstEmpty() == -1) {
            player.sendMessage(fileManager.getMsg("give.fulleq"));
            return true;
        }
        try {
            final ItemStack material = MaterialUtil.getMaterial(args);
            player.getInventory().addItem(material);
            player.sendMessage(fileManager.getMsg("give.give").replace("{ITEM}", material.getType().name()).replace("{AMOUNT}", String.valueOf(material.getAmount())).replace("{DATA}", String.valueOf(material.getData().getData())));
        } catch (NullPointerException | ArrayIndexOutOfBoundsException exception) {
            sender.sendMessage(fileManager.getMsg("item.usage"));
        }
        return true;
    }
}
