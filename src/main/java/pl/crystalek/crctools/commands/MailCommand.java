package pl.crystalek.crctools.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.exceptions.MailExistException;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.MailManager;
import pl.crystalek.crctools.model.Mail;
import pl.crystalek.crctools.utils.ChatUtil;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public final class MailCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final MailManager mailManager;

    public MailCommand(final FileManager fileManager, final MailManager mailManager) {
        this.fileManager = fileManager;
        this.mailManager = mailManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length >= 4) {
            if (args[0].equalsIgnoreCase("send")) {
                if (sender.getName().equalsIgnoreCase(args[1])) {
                    sender.sendMessage(fileManager.getMsg("mail.sendsameplayer"));
                    return true;
                }
                final String join = StringUtils.join(args, ' ', 3, args.length);
                try {
                    mailManager.sendMessage(sender.getName(), args[1], args[2], join);
                    sender.sendMessage(fileManager.getMsg("mail.mailsent"));
                    if (Bukkit.getPlayer(args[1]) != null) {
                        Bukkit.getPlayer(args[1]).sendMessage(fileManager.getMsg("mail.sendtoplayer").replace("{PLAYER}", sender.getName()));
                    }
                } catch (final IOException exception) {
                    exception.printStackTrace();
                } catch (final MailExistException exception) {
                    sender.sendMessage(fileManager.getMsg("mail.senderror"));
                } catch (final NullPointerException exception) {
                    sender.sendMessage(fileManager.getMsg("cantexist"));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (!((args[2].equalsIgnoreCase("topicsent") || args[2].equalsIgnoreCase("topicreceived")))) {
                    printHelp(sender);
                    return true;
                }
                try {
                    mailManager.removeMail((Player) sender, args[2], args[3]);
                    sender.sendMessage(fileManager.getMsg("mail.removemail"));
                } catch (final IOException exception) {
                    exception.printStackTrace();
                } catch (final NullPointerException exception) {
                    sender.sendMessage(fileManager.getMsg("cantexist"));
                } catch (final MailExistException exception) {
                    sender.sendMessage(fileManager.getMsg("mail.removeerror"));
                }
            } else {
                printHelp(sender);
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("read")) {
                try {
                    final Mail mail = mailManager.readMessage(sender.getName(), args[1], args[2]);
                    if (mail == null) {
                        sender.sendMessage(fileManager.getMsg("mail.readerror"));
                        return true;
                    }
                    final String mailMessage = mail.getMail();
                    final String join = String.join("\n", fileManager.getMsgList("mail.clearhover")).replace("{AUTHOR}", args[1]).replace("{DATE}", mail.getSendMessageTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).replace("{MESSAGE}", mailMessage);
                    ChatUtil.sendHoverMessage((Player) sender, fileManager.getMsg("mail.read").replace("{MESSAGE}", mailMessage), join);
                } catch (final IOException exception) {
                    exception.printStackTrace();
                } catch (final NullPointerException exception) {
                    exception.printStackTrace();
                    sender.sendMessage(fileManager.getMsg("cantexist"));
                }
            } else {
                printHelp(sender);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("clear")) {
                try {
                    mailManager.clearMails((Player) sender, args[1]);
                    sender.sendMessage(fileManager.getMsg("mail.clear"));
                } catch (final IOException exception) {
                    exception.printStackTrace();
                } catch (final NullPointerException exception) {
                    printHelp(sender);
                }
            } else {
                printHelp(sender);
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("sentlist")) {
                final Map<UUID, List<Mail>> mails = mailManager.getSentMails((Player) sender);
                if (mails.isEmpty()) {
                    sender.sendMessage(fileManager.getMsg("mail.sentlisterror"));
                    return true;
                }
                printList(mails, (player, mail) -> {
                    final String mailMessage = mail.getMail();
                    final String join = String.join("\n", fileManager.getMsgList("mail.sentlisthover")).replace("{DATE}", mail.getSendMessageTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).replace("{MESSAGE}", mailMessage).replace("{READ}", String.valueOf(mail.isRead()).replace("true", "tak").replace("false", "nie")).replace("{TOPIC}", mail.getTopic()).replace("{RECEIVER}", player.getName());
                    ChatUtil.sendHoverMessage((Player) sender, fileManager.getMsg("mail.read").replace("{MESSAGE}", mailMessage), join);
                });
            } else if (args[0].equalsIgnoreCase("readlist")) {
                final Map<UUID, List<Mail>> mails = mailManager.getReadMails((Player) sender);
                if (mails.isEmpty()) {
                    sender.sendMessage(fileManager.getMsg("mail.readlisterror"));
                    return true;
                }
                printList(mails, (player, mail) -> {
                    final String mailMessage = mail.getMail();
                    final String join = String.join("\n", fileManager.getMsgList("mail.readlisthover")).replace("{AUTHOR}", player.getName()).replace("{DATE}", mail.getSendMessageTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).replace("{MESSAGE}", mailMessage).replace("{TOPIC}", mail.getTopic());
                    ChatUtil.sendHoverMessage((Player) sender, fileManager.getMsg("mail.read").replace("{MESSAGE}", mailMessage), join);
                });
            } else if (args[0].equalsIgnoreCase("list")) {
                final Map<UUID, List<Mail>> mails = mailManager.getMails((Player) sender);
                if (mails.isEmpty()) {
                    sender.sendMessage(fileManager.getMsg("mail.listerror"));
                    return true;
                }
                printList(mails, (player, mail) -> {
                    final String join = String.join("\n", fileManager.getMsgList("mail.listhover")).replace("{AUTHOR}", player.getName()).replace("{DATE}", mail.getSendMessageTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    ChatUtil.sendHoverMessage((Player) sender, fileManager.getMsg("mail.topictolist").replace("{TOPIC}", mail.getTopic()), join);
                });
            } else {
                printHelp(sender);
            }
        } else {
            printHelp(sender);
        }
        return true;
    }

    private void printHelp(final CommandSender sender) {
        final List<String> msgList = fileManager.getMsgList("mail.help");
        for (final String message : msgList) {
            sender.sendMessage(message);
        }
    }

    public void printList(final Map<UUID, List<Mail>> uuidListMap, final BiConsumer<OfflinePlayer, Mail> consumer) {
        for (final UUID uuid : uuidListMap.keySet()) {
            final OfflinePlayer player;
            if (Bukkit.getPlayer(uuid) != null) {
                player = Bukkit.getPlayer(uuid);
            } else {
                player = Bukkit.getOfflinePlayer(uuid);
            }
            for (final Mail mail : uuidListMap.get(uuid)) {
                consumer.accept(player, mail);
            }
        }
    }
}