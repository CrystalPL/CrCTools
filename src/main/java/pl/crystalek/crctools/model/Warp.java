package pl.crystalek.crctools.model;

import org.bukkit.Location;

import java.time.LocalDateTime;

public class Warp {
    private LocalDateTime createTime;
    private String author;
    private Location location;

    public Warp(final LocalDateTime createTime, final String author, final Location location) {
        this.createTime = createTime;
        this.author = author;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public String getAuthor() {
        return author;
    }
}
