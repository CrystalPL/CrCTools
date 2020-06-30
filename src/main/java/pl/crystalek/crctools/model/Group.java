package pl.crystalek.crctools.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Group {
    private final LocalDateTime createTime;
    private final String author;
    private final String prefix;
    private List<String> members = new ArrayList<>();
    private Set<String> permissions = new HashSet<>();

    public Group(final LocalDateTime createTime, final String author, final String prefix, final List<String> members, final List<String> permissions) {
        this.createTime = createTime;
        this.author = author;
        this.prefix = prefix;
        this.members = members;
        this.permissions = new HashSet<>(permissions);
    }

    public Group(final LocalDateTime createTime, final String author, final String prefix) {
        this.createTime = createTime;
        this.author = author;
        this.prefix = prefix;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public String getAuthor() {
        return author;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<String> getMembers() {
        return members;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void addPermission(final String permissionName) {
        permissions.add(permissionName);
    }

    public void removePermission(final String permissionName) {
        permissions.remove(permissionName);
    }

    public void setPermissions(final List<String> permissions) {
        this.permissions = new HashSet<>(permissions);
    }
}
