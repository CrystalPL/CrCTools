package pl.crystalek.crctools.managers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.CrCTools;
import pl.crystalek.crctools.exceptions.GroupExistException;
import pl.crystalek.crctools.exceptions.GroupHasException;
import pl.crystalek.crctools.model.Group;
import pl.crystalek.crctools.model.User;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class PermissionManager {
    private final FileManager fileManager;
    private final CrCTools crCTools;
    private final UserManager userManager;
    private final Map<String, Group> groups = new HashMap<>();
    //TODO DODANIE RANG TYMCZASOWYCH

    public PermissionManager(final FileManager fileManager, final CrCTools crCTools, final UserManager userManager) {
        this.fileManager = fileManager;
        this.crCTools = crCTools;
        this.userManager = userManager;
    }

    public void addGroup(final String player, final String groupName) throws IOException, GroupExistException, GroupHasException {
        if (!groups.containsKey(groupName)) {
            throw new GroupExistException("Group doesn't exist!");
        }
        if (Bukkit.getPlayer(player) != null) {
            final User user = userManager.getUser(Bukkit.getPlayer(player));
            if (user.getPermissionGroups().contains(groupName)) {
                throw new GroupHasException("This player has a this group");
            }
            user.getPermissionGroups().add(groupName);
        }
        final YamlConfiguration playerFile = fileManager.getPlayerFile(player);
        final List<String> groups = playerFile.getStringList("groups");
        if (groups.contains(groupName)) {
            throw new GroupExistException("This player has a this group");
        }
        groups.add(groupName);
        playerFile.set("groups", groups);
        final List<String> members = getGroup(groupName).getMembers();
        members.add(player);
        crCTools.getConfig().set("groups." + groupName + ".members", members);
        crCTools.saveConfig();
        playerFile.save(new File(fileManager.getUsers(), player + ".yml"));

        final Set<String> groupPermission = getGroupPermission(groupName);
        for (final String permission : groupPermission) {
            addPermission(player, permission);
        }
    }

    public void removeGroup(final String player, final String groupName) throws IOException, GroupExistException, GroupHasException {
        if (!groups.containsKey(groupName)) {
            throw new GroupExistException("Group doesn't exist!");
        }
        if (Bukkit.getPlayer(player) != null) {
            final User user = userManager.getUser(Bukkit.getPlayer(player));
            if (!user.getPermissionGroups().contains(groupName)) {
                throw new GroupHasException("This player has a this group");
            }
            user.getPermissionGroups().remove(groupName);
        }
        final YamlConfiguration playerFile = fileManager.getPlayerFile(player);
        final List<String> groups = playerFile.getStringList("groups");
        groups.remove(groupName);
        playerFile.set("groups", groups);
        getGroup(groupName).getMembers().remove(player);
        crCTools.getConfig().set("groups." + groupName + ".members", getGroup(groupName).getMembers());
        crCTools.saveConfig();
        playerFile.save(new File(fileManager.getUsers(), player + ".yml"));

        final Set<String> groupPermission = getGroupPermission(groupName);
        for (final String permission : groupPermission) {
            removePermission(player, permission);
        }
    }

    public void clear(final String player, final boolean groupsOrPermissions) throws IOException, GroupHasException, GroupExistException {
        final List<String> deleteElement;
        final YamlConfiguration playerFile = fileManager.getPlayerFile(player);
        if (groupsOrPermissions) {
            if (Bukkit.getPlayer(player) != null) {
                deleteElement = new ArrayList<>(userManager.getUser(Bukkit.getPlayer(player)).getPermissionAttachment().getPermissions().keySet());
            } else {
                deleteElement = playerFile.getStringList("permissions");
            }
            for (final String permission : deleteElement) {
                removePermission(player, permission);
            }
        } else {
            if (Bukkit.getPlayer(player) != null) {
                deleteElement = new ArrayList<>(userManager.getUser(Bukkit.getPlayer(player)).getPermissionGroups());
            } else {
                deleteElement = playerFile.getStringList("permissions");
            }
            for (final String groupName : deleteElement) {
                removeGroup(player, groupName);
            }
        }
    }

    public void addGroupPermission(final String groupName, final String permission) throws GroupExistException {
        if (!groups.containsKey(groupName)) {
            throw new GroupExistException("Group doesn't!");
        }
        final FileConfiguration config = crCTools.getConfig();
        getGroup(groupName).addPermission(permission);
        config.set("groups." + groupName + ".permissions", new ArrayList<>(getGroupPermission(groupName)));
        crCTools.saveConfig();
        //TODO DODANIE MOZLIWOSCI DODAWANIA DODANEGO UPRAWNIENIA WSZYSTKI MGRACZOM KTORZY POSIADAJA OWA GRUPE
    }

    public void removeGroupPermission(final String groupName, final String permission) throws GroupExistException {
        if (!groups.containsKey(groupName)) {
            throw new GroupExistException("Group doesn't exist!");
        }
        final FileConfiguration config = crCTools.getConfig();
        getGroup(groupName).removePermission(permission);
        config.set("groups." + groupName + ".permissions", new ArrayList<>(getGroupPermission(groupName)));
        crCTools.saveConfig();
        //TODO DODANIE MOZLIWOSCI USUWANIA USUNIETEGO UPRAWNIENIA WSZYSTKI MGRACZOM KTORZY POSIADAJA OWA GRUPE
    }

    public void cloneGroup(final String fromGroup, final String toGroup) throws GroupExistException {
        if (!groups.containsKey(fromGroup)) {
            throw new GroupExistException("Group doesn't exist!");
        }
        if (!groups.containsKey(toGroup)) {
            throw new GroupExistException("Group doesn't exist!");
        }
        final FileConfiguration config = crCTools.getConfig();
        final Set<String> groupPermissionFrom = getGroupPermission(fromGroup);
        final Set<String> groupPermissionTo = getGroupPermission(toGroup);
        final List<String> permissions = new ArrayList<>();
        permissions.addAll(groupPermissionFrom);
        permissions.addAll(groupPermissionTo);
        config.set("groups." + toGroup + ".permissions", permissions);
        getGroup(toGroup).setPermissions(permissions);
        crCTools.saveConfig();
    }

    public void createGroup(final String player, final String groupName, final byte priority) throws GroupExistException {
        if (groups.containsKey(groupName)) {
            throw new GroupExistException("Group doesn't exist!");
        }
        final FileConfiguration config = crCTools.getConfig();
        final String groupPath = "groups." + groupName;
        final LocalDateTime localDateTime = LocalDateTime.now();
        final String date = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        groups.put(groupName, new Group(localDateTime, player, groupName, priority));
        config.createSection(groupPath);
        config.set(groupPath + ".date", date);
        config.set(groupPath + ".author", player);
        config.set(groupPath + ".prefix", groupName);
        config.set(groupPath + ".priority", priority);
        config.createSection(groupPath + ".permissions");
        config.createSection(groupPath + ".members");
        crCTools.saveConfig();
    }

    public void deleteGroup(final String groupName) throws GroupExistException, IOException, GroupHasException {
        if (!groups.containsKey(groupName)) {
            throw new GroupExistException("Group doesn't exist!");
        }
        final List<String> members = new ArrayList<>(getGroup(groupName).getMembers());
        for (final String member : members) {
            removeGroup(member, groupName);
            //TODO DODANIE MOZLIWOSCI WYBRANIA, CZY UPRAWNIENIA GRACZY MAJA ZOSTAC USUNIETE
        }
        crCTools.getConfig().set("groups." + groupName, null);
        groups.remove(groupName);
        crCTools.saveConfig();
    }

    public void addPermission(final String player, final String permissionName) throws IOException {
        final YamlConfiguration playerFile = fileManager.getPlayerFile(player);
        final Set<String> permissions = new HashSet<>(playerFile.getStringList("permissions"));
        if (Bukkit.getPlayer(player) != null) {
            userManager.getUser(Bukkit.getPlayer(player)).getPermissionAttachment().setPermission(permissionName, true);
        }
        permissions.add(permissionName);
        playerFile.set("permissions", new ArrayList<>(permissions));
        playerFile.save(new File(fileManager.getUsers(), player + ".yml"));
    }

    public void removePermission(final String player, final String permissionName) throws IOException {
        final YamlConfiguration playerFile = fileManager.getPlayerFile(player);
        final Set<String> permissions = new HashSet<>(playerFile.getStringList("permissions"));
        if (Bukkit.getPlayer(player) != null) {
            userManager.getUser(Bukkit.getPlayer(player)).getPermissionAttachment().unsetPermission(permissionName);
        }
        permissions.remove(permissionName);
        playerFile.set("permissions", new ArrayList<>(permissions));
        playerFile.save(new File(fileManager.getUsers(), player + ".yml"));
    }

    public void loadGroups() {
        final FileConfiguration config = crCTools.getConfig();
        if (config.getConfigurationSection("groups") != null) {
            final ConfigurationSection configurationSection = config.getConfigurationSection("groups");
            final List<String> keyList = new ArrayList<>(configurationSection.getKeys(false));
            for (String string : keyList) {
                final String name = string;
                string = "groups." + string;
                groups.put(name,
                        new Group(LocalDateTime.parse(config.getString(string + ".date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                config.getString(string + ".author"),
                                config.getString(string + ".prefix"),
                                (byte) config.getInt(string + ".priority"),
                                config.getStringList(string + ".members"),
                                config.getStringList(string + ".permissions")));
            }
        }
    }

    public void loadPermission(final Player player) {
        final YamlConfiguration playerFile = fileManager.getPlayerFile(player.getName());
        final List<String> permissions = playerFile.getStringList("permissions");
        for (final String permission : permissions) {
            userManager.getUser(player).getPermissionAttachment().setPermission(permission, true);
        }
    }

    public Group getGroup(final String groupName) {
        return groups.get(groupName);
    }

    public Set<String> getGroupPermission(final String groupName) {
        return getGroup(groupName).getPermissions();
    }

    public Map<String, Group> getGroups() {
        return groups;
    }

    public void setPrefix(final String groupName, final String prefix) throws GroupExistException {
        if (!groups.containsKey(groupName)) {
            throw new GroupExistException("Group doesn't exist!");
        }
        getGroup(groupName).setPrefix(prefix);
        final FileConfiguration config = crCTools.getConfig();
        config.set("groups." + groupName + ".prefix", prefix);
        crCTools.saveConfig();
    }

    public void setPriority(final String groupName, final byte priority) throws GroupExistException {
        if (!groups.containsKey(groupName)) {
            throw new GroupExistException("Group doesn't exist!");
        }
        getGroup(groupName).setPriority(priority);
        final FileConfiguration config = crCTools.getConfig();
        config.set("groups." + groupName + ".priority", priority);
        crCTools.saveConfig();
    }
}