package pl.crystalek.crctools.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.exceptions.MailExistException;
import pl.crystalek.crctools.model.Mail;
import pl.crystalek.crctools.model.User;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;

public final class MailManager {
    private final FileManager fileManager;
    private final UserManager userManager;

    public MailManager(final FileManager fileManager, final UserManager userManager) {
        this.fileManager = fileManager;
        this.userManager = userManager;
    }

    public void sendMessage(final String senderName, final String receiverName, final String topic, final String message) throws IOException, MailExistException, IllegalArgumentException {
        //TODO DODANIE LIMITU WYSYLANYCH/ODBIERANYCH MAILI DLA POSZCZEGOLNEJ RANGI
        if (topic.contains(".")) {
            throw new IllegalArgumentException("you cannot use a period!");
        }
        final YamlConfiguration playerSender = fileManager.getPlayerFile(senderName);
        final YamlConfiguration playerReceiver = fileManager.getPlayerFile(receiverName);
        ConfigurationSection sentMails = playerSender.getConfigurationSection("mail.sent");
        ConfigurationSection receivedMails = playerReceiver.getConfigurationSection("mail.received");
        final LocalDateTime localDateTime = LocalDateTime.now();
        final String date = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        final Player playerS = Bukkit.getPlayer(senderName);
        final UUID receiverID = Bukkit.getOfflinePlayer(receiverName).getUniqueId();
        final Map<UUID, List<Mail>> sentMailsMap = userManager.getUser(playerS).getSentMails();
        List<Mail> mailSent = sentMailsMap.get(Bukkit.getOfflinePlayer(receiverName).getUniqueId());
        final Mail mail = new Mail(localDateTime, message, false, topic);

        if (mailSent == null) {
            mailSent = new ArrayList<>();
        }
        for (final Mail foundTopic : mailSent) {
            if (foundTopic.getTopic().equals(topic)) {
                throw new MailExistException("This player has a this topic");
            }
        }
        if (Bukkit.getPlayer(receiverName) != null) {
            final Map<UUID, List<Mail>> receivedMailsMap = userManager.getUser(Bukkit.getPlayer(receiverName)).getReceivedMails();
            List<Mail> mailReceived = receivedMailsMap.get(playerS.getUniqueId());
            if (mailReceived == null) {
                mailReceived = new ArrayList<>();
            }
            mailReceived.add(mail);
            receivedMailsMap.put(playerS.getUniqueId(), mailReceived);
            userManager.getUser(Bukkit.getPlayer(receiverName)).setReceivedMails(receivedMailsMap);
        }

        mailSent.add(mail);
        sentMailsMap.put(receiverID, mailSent);
        if (sentMails == null) {
            sentMails = playerSender.createSection("mail.sent");
        }
        if (receivedMails == null) {
            receivedMails = playerReceiver.createSection("mail.received");
        }
        sentMails.set(receiverName + "." + topic + ".sendtime", date);
        sentMails.set(receiverName + "." + topic + ".contents", message);
        sentMails.set(receiverName + "." + topic + ".read", false);
        receivedMails.set(senderName + "." + topic + ".sendtime", date);
        receivedMails.set(senderName + "." + topic + ".contents", message);
        receivedMails.set(senderName + "." + topic + ".read", false);

        userManager.getUser(playerS).setSentMails(sentMailsMap);
        playerSender.save(new File(fileManager.getUsers(), senderName + ".yml"));
        playerReceiver.save(new File(fileManager.getUsers(), receiverName + ".yml"));
    }

    public short loadMessage(final Player player) {
        final YamlConfiguration playerFile = fileManager.getPlayerFile(player.getName());
        final ConfigurationSection sentMails = playerFile.getConfigurationSection("mail.sent");
        final ConfigurationSection receivedMails = playerFile.getConfigurationSection("mail.received");
        final Map<UUID, List<Mail>> mapSentMails = new HashMap<>();
        final Map<UUID, List<Mail>> mapReceivedMails = new HashMap<>();
        final List<Mail> sentList = new ArrayList<>();
        final List<Mail> receivedList = new ArrayList<>();
        short counter = 0;

        if (receivedMails != null) {
            for (final String mail : receivedMails.getKeys(false)) {
                final UUID uniqueId;
                if (Bukkit.getPlayer(mail) != null) {
                    uniqueId = Bukkit.getPlayer(mail).getUniqueId();
                } else {
                    uniqueId = Bukkit.getOfflinePlayer(mail).getUniqueId();
                }
                for (final String topic : receivedMails.getConfigurationSection(mail).getKeys(false)) {
                    counter++;
                    final String string = mail + "." + topic;

                    final LocalDateTime parse = LocalDateTime.parse(receivedMails.getString(string + ".sendtime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    receivedList.add(new Mail(parse, receivedMails.getString(string + ".contents"), receivedMails.getBoolean(string + ".read"), topic));
                }
                mapReceivedMails.put(uniqueId, receivedList);
            }
            userManager.getUser(player).setReceivedMails(mapReceivedMails);
        }
        if (sentMails != null) {
            for (final String mail : sentMails.getKeys(false)) {
                final UUID uniqueId;
                if (Bukkit.getPlayer(mail) != null) {
                    uniqueId = Bukkit.getPlayer(mail).getUniqueId();
                } else {
                    uniqueId = Bukkit.getOfflinePlayer(mail).getUniqueId();
                }
                for (final String topic : sentMails.getConfigurationSection(mail).getKeys(false)) {
                    final String string = mail + "." + topic;
                    sentList.add(new Mail(LocalDateTime.parse(sentMails.getString(string + ".sendtime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), sentMails.getString(string + ".contents"), sentMails.getBoolean(string + ".read"), topic));
                }
                mapSentMails.put(uniqueId, sentList);
            }
            userManager.getUser(player).setSentMails(mapSentMails);
        }
        return counter;
    }

    public Mail readMessage(final String playerSender, final String playerSentMail, final String topic) throws IOException {
        //TODO DODANIE DATY ODCZYTANIA WIADOMOSCI
        final UUID uniqueId;
        if (Bukkit.getPlayer(playerSentMail) != null) {
            uniqueId = Bukkit.getPlayer(playerSentMail).getUniqueId();
        } else {
            uniqueId = Bukkit.getOfflinePlayer(playerSentMail).getUniqueId();
        }
        final Player player = Bukkit.getPlayer(playerSender);
        if (Bukkit.getPlayer(playerSentMail) != null) {
            final List<Mail> mailList = userManager.getUser(Bukkit.getPlayer(playerSentMail)).getSentMails().get(player.getUniqueId());
            if (mailList != null) {
                for (final Mail mail : mailList) {
                    if (mail.getTopic().equalsIgnoreCase(topic)) {
                        mail.setRead(true);
                        break;
                    }
                }
            }
        }
        final YamlConfiguration playerFile = fileManager.getPlayerFile(playerSentMail);
        playerFile.set("mail.sent." + playerSender + "." + topic + ".read", true);
        final List<Mail> mailList = userManager.getUser(Bukkit.getPlayer(playerSender)).getReceivedMails().get(uniqueId);
        for (final Mail mail : mailList) {
            if (mail.getTopic().equalsIgnoreCase(topic)) {
                mail.setRead(true);
                return mail;
            }
        }
        playerFile.save(new File(fileManager.getUsers(), playerSender + ".yml"));
        return null;
    }

    public Map<UUID, List<Mail>> getSentMails(final Player player) {
        return userManager.getUser(player).getSentMails();
    }

    public Map<UUID, List<Mail>> getReadMails(final Player player) {
        final Map<UUID, List<Mail>> receivedMails = userManager.getUser(player).getReceivedMails();
        return getMailsHelper(receivedMails, (mail, list) -> {
            if (mail.isRead()) {
                list.add(mail);
            }
        });
    }

    public Map<UUID, List<Mail>> getMails(final Player player) {
        final Map<UUID, List<Mail>> receivedMails = userManager.getUser(player).getReceivedMails();
        return getMailsHelper(receivedMails, (mail, list) -> {
            if (!mail.isRead()) {
                list.add(mail);
            }
        });
    }

    private Map<UUID, List<Mail>> getMailsHelper(final Map<UUID, List<Mail>> uuidListMap, final BiConsumer<Mail, List<Mail>> consumer) {
        final Map<UUID, List<Mail>> mapToReturn = new HashMap<>();
        final List<Mail> listToReturn = new ArrayList<>();
        for (final UUID uuid : uuidListMap.keySet()) {
            for (final Mail mail : uuidListMap.get(uuid)) {
                consumer.accept(mail, listToReturn);
            }
            if (!listToReturn.isEmpty()) {
                mapToReturn.put(uuid, listToReturn);
            }
        }
        return mapToReturn;
    }

    public void removeMail(final Player playerSender, final String option, final String topic) throws IOException, MailExistException {
        final User user = userManager.getUser(playerSender);
        final String player = playerSender.getName();
        final YamlConfiguration playerFile = fileManager.getPlayerFile(player);
        if (option.equalsIgnoreCase("topicsent")) {
            final Map<UUID, List<Mail>> sentMails = user.getSentMails();
            removeMailTopic(sentMails, topic, (playerReceiver, mail) -> playerFile.set("mail.sent." + playerReceiver.getName() + "." + mail.getTopic(), null));
        } else if (option.equalsIgnoreCase("topicreceived")) {
            final Map<UUID, List<Mail>> receivedMails = user.getReceivedMails();
            removeMailTopic(receivedMails, topic, (playerReceiver, mail) -> playerFile.set("mail.received." + playerReceiver.getName() + "." + mail.getTopic(), null));
        }
        playerFile.save(new File(fileManager.getUsers(), player + ".yml"));
    }

    private void removeMailTopic(final Map<UUID, List<Mail>> uuidListMap, final String topic, final BiConsumer<OfflinePlayer, Mail> consumer) throws MailExistException {
        for (final UUID uuid : uuidListMap.keySet()) {
            final OfflinePlayer playerReceiver;
            if (Bukkit.getPlayer(uuid) != null) {
                playerReceiver = Bukkit.getPlayer(uuid);
            } else {
                playerReceiver = Bukkit.getOfflinePlayer(uuid);
            }
            final Iterator<Mail> iterator = uuidListMap.get(uuid).iterator();
            boolean stopIterate = false;
            while (iterator.hasNext()) {
                final Mail next = iterator.next();
                if (next.getTopic().equals(topic)) {
                    consumer.accept(playerReceiver, next);
                    iterator.remove();
                    stopIterate = true;
                    break;
                }
            }
            if (!stopIterate) {
                throw new MailExistException("This mails not exist");
            }
        }
    }

    public void clearMails(final Player playerSender, final String option) throws IOException {
        //TODO CZYSZCZENIE WIADOMOSCI OD KONKRETNEGO GRACZA
        final User user = userManager.getUser(playerSender);
        final YamlConfiguration playerFile = fileManager.getPlayerFile(playerSender.getName());
        if (option.equalsIgnoreCase("*")) {
            user.getSentMails().clear();
            user.getReceivedMails().clear();
            playerFile.set("mail.received", null);
            playerFile.set("mail.sent", null);
        } else if (option.equalsIgnoreCase("sent")) {
            user.getSentMails().clear();
            playerFile.set("mail.sent", null);
        } else if (option.equalsIgnoreCase("read")) {
            user.getReceivedMails().clear();
            playerFile.set("mail.received", null);
        } else {
            throw new NullPointerException("error option");
        }
        playerFile.save(new File(fileManager.getUsers(), playerSender.getName() + ".yml"));
    }
}
