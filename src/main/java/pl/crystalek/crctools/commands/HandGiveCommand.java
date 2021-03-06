package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.crystalek.crctools.exceptions.NotDetectedItemException;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.utils.MaterialUtil;
import pl.crystalek.crctools.utils.NumberUtil;

public final class HandGiveCommand implements CommandExecutor {
    private final FileManager fileManager;

    public HandGiveCommand(final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("give.give"))) {
            sender.sendMessage(fileManager.getMsgPermission("give.give"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return true;
        }
        short amountItem;
        if (args.length == 1) {
            amountItem = 64;
        } else if (args.length == 2) {
            if (!NumberUtil.isInt(args[1])) {
                sender.sendMessage(fileManager.getMsg("speed.errornumber"));
                return true;
            }
            try {
                amountItem = Short.parseShort(args[1]);
            } catch (final NumberFormatException exception) {
                sender.sendMessage(fileManager.getMsg("give.numberoutofrange"));
                return true;
            }
        } else {
            sender.sendMessage(fileManager.getMsg("handgive.usage"));
            return true;
        }
        if (args[0].equalsIgnoreCase("*")) {
            final ItemStack itemInHand = ((Player) sender).getItemInHand();
            int counterGive = 0;
            for (final Player player : Bukkit.getOnlinePlayers()) {
                final PlayerInventory inventory = player.getInventory();
                final ItemStack material;
                try {
                    material = MaterialUtil.getMaterialHand(inventory, itemInHand, amountItem);
                    counterGive++;
                } catch (final NullPointerException | ArrayIndexOutOfBoundsException | IllegalArgumentException exception) {
                    sender.sendMessage(fileManager.getMsg("handgive.usage"));
                    return true;
                }
                inventory.addItem(material);
                player.sendMessage(fileManager.getMsg("give.give").replace("{ITEM}", material.getType().name()).replace("{AMOUNT}", String.valueOf(amountItem)).replace("{DATA}", String.valueOf(material.getData().getData())));
            }
            sender.sendMessage(fileManager.getMsg("give.all").replace("{PLAYERGIVE}", String.valueOf(Bukkit.getOnlinePlayers().size())).replace("{PLAYERTOGIVE}", String.valueOf(counterGive)));
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(fileManager.getMsg("offlineplayer"));
            return true;
        }
        final Player target = Bukkit.getPlayer(args[0]);
        final PlayerInventory inventory = target.getInventory();
        final ItemStack material;
        try {
            material = MaterialUtil.getMaterialHand(inventory, ((Player) sender).getItemInHand(), amountItem);
        } catch (final NullPointerException | ArrayIndexOutOfBoundsException | IllegalArgumentException exception) {
            sender.sendMessage(fileManager.getMsg("give.usage"));
            return true;
        } catch (final NotDetectedItemException exception) {
            sender.sendMessage(fileManager.getMsg("give.fulleq"));
            return true;
        }
        inventory.addItem(material);
        target.sendMessage(fileManager.getMsg("give.give").replace("{ITEM}", material.getType().name()).replace("{AMOUNT}", String.valueOf(amountItem)).replace("{DATA}", String.valueOf(material.getData().getData())));
        sender.sendMessage(fileManager.getMsg("give.player").replace("{ITEM}", material.getType().name()).replace("{AMOUNT}", String.valueOf(amountItem)).replace("{DATA}", String.valueOf(material.getData().getData())).replace("{PLAYER}", target.getName()));
        return true;
    }
}
