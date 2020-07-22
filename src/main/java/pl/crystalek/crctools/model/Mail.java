package pl.crystalek.crctools.model;

import java.time.LocalDateTime;

public class Mail {
    private final LocalDateTime sendMessageTime;
    private final String mail;
    private final String topic;
    private boolean read;

    public Mail(final LocalDateTime sendMessageTime, final String mail, final boolean read, final String topic) {
        this.sendMessageTime = sendMessageTime;
        this.mail = mail;
        this.read = read;
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "sendMessageTime=" + sendMessageTime +
                ", mail='" + mail + '\'' +
                ", topic='" + topic + '\'' +
                ", read=" + read +
                '}';
    }

    public String getTopic() {
        return topic;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getSendMessageTime() {
        return sendMessageTime;
    }

    public String getMail() {
        return mail;
    }
}
