package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.MaterialUtil;
import pl.crystalek.crctools.utils.NumberUtil;

public class GiveCommand implements CommandExecutor {
    private final FileManager fileManager;

    public GiveCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("give.give"))) {
            sender.sendMessage(fileManager.getMsgPermission("give.give"));
            return true;
        }
        if (args.length != 3) {
            sender.sendMessage(fileManager.getMsg("give.usage"));
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(fileManager.getMsg("offlineplayer"));
            return true;
        }
        if (!NumberUtil.isInt(args[2])) {
            sender.sendMessage(fileManager.getMsg("speed.errornumber"));
            return true;
        }
        final Player target = Bukkit.getPlayer(args[0]);
        final PlayerInventory inventory = target.getInventory();
        if (inventory.firstEmpty() == -1) {
            sender.sendMessage(fileManager.getMsg("give.fulleq"));
            return true;
        }

        try {
            final ItemStack material = MaterialUtil.getMaterial(args);
            target.getInventory().addItem(material);
            target.sendMessage(fileManager.getMsg("give.give").replace("{ITEM}", material.getType().name()).replace("{AMOUNT}", String.valueOf(material.getAmount())).replace("{DATA}", String.valueOf(material.getData().getData())));
        } catch (NullPointerException exception) {
            sender.sendMessage(fileManager.getMsg("give.usage"));
        }
        return true;
    }
}