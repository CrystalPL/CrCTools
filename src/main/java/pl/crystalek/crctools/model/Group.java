package pl.crystalek.crctools.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Group implements Comparable<Group> {
    private final LocalDateTime createTime;
    private final String author;
    private String prefix;
    private List<String> members = new ArrayList<>();
    private Set<String> permissions = new HashSet<>();
    private byte priority;
    private String format = "{PREFIX} &a&l{NICK} &8» {MESSAGE}";

    public Group(final LocalDateTime createTime, final String author, final String prefix, final byte priority, final List<String> members, final List<String> permissions, final String format) {
        this.createTime = createTime;
        this.author = author;
        this.prefix = prefix;
        this.priority = priority;
        this.members = members;
        this.permissions = new HashSet<>(permissions);
        this.format = format;
    }

    public Group(final LocalDateTime createTime, final String author, final String prefix, final byte priority) {
        this.createTime = createTime;
        this.author = author;
        this.prefix = prefix;
        this.priority = priority;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(final String format) {
        this.format = format;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(final byte priority) {
        this.priority = priority;
    }

    public String getAuthor() {
        return author;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public List<String> getMembers() {
        return members;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(final List<String> permissions) {
        this.permissions = new HashSet<>(permissions);
    }

    public void addPermission(final String permissionName) {
        permissions.add(permissionName);
    }

    public void removePermission(final String permissionName) {
        permissions.remove(permissionName);
    }

    @Override
    public int compareTo(final Group group) {
        return Byte.compare(group.priority, priority);
    }
}
