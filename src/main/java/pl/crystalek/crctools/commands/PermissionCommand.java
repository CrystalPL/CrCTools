package pl.crystalek.crctools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.crystalek.crctools.exceptions.GroupExistException;
import pl.crystalek.crctools.exceptions.GroupHasException;
import pl.crystalek.crctools.managers.FileManager;
import pl.crystalek.crctools.managers.PermissionManager;
import pl.crystalek.crctools.managers.UserManager;
import pl.crystalek.crctools.model.Group;
import pl.crystalek.crctools.model.User;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class PermissionCommand implements CommandExecutor {
    private final FileManager fileManager;
    private final PermissionManager permissionManager;
    private final UserManager userManager;

    public PermissionCommand(final FileManager fileManager, final PermissionManager permissionManager, final UserManager userManager) {
        this.fileManager = fileManager;
        this.permissionManager = permissionManager;
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission(fileManager.getPermission("permissions.permission"))) {
            sender.sendMessage(fileManager.getMsgPermission("permissions.permission"));
            return true;
        }
        if (args.length < 2) {
            printHelp(sender);
            return true;
        }
        if (args[0].equalsIgnoreCase("group")) {
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("delete")) {
                    try {
                        permissionManager.deleteGroup(args[2]);
                        sender.sendMessage(fileManager.getMsg("permissions.deletegroup").replace("{NAME}", args[2]));
                    } catch (final GroupExistException exception) {
                        sender.sendMessage(fileManager.getMsg("permissions.groupdoesntexist"));
                    } catch (final IOException | GroupHasException exception) {
                        exception.printStackTrace();
                    }
                } else if (args[1].equalsIgnoreCase("info")) {
                    if (permissionManager.getGroup(args[2]) == null) {
                        sender.sendMessage(fileManager.getMsg("permissions.groupdoesntexist"));
                        return true;
                    }
                    final Group group = permissionManager.getGroup(args[2]);
                    final List<String> msgList = fileManager.getMsgList("permissions.groupinfo");
                    final List<String> members = group.getMembers();
                    String membersList = members.stream().collect(Collectors.joining(fileManager.getMsg("interlude")));
                    if (members.isEmpty()) {
                        membersList = fileManager.getMsg("permissions.emptymemberingroup");
                    }
                    for (final String message : msgList) {
                        sender.sendMessage(message
                                .replace("{NAME}", args[2])
                                .replace("{PLAYER}", group.getAuthor())
                                .replace("{DATE}", group.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                .replace("{PREFIX}", group.getPrefix())
                                .replace("{PRIORITY}", String.valueOf(group.getPriority()))
                                .replace("{MEMBERS}", membersList));
                    }
                } else if (args[1].equalsIgnoreCase("permissions")) {
                    final Group group = permissionManager.getGroup(args[2]);
                    final Set<String> permissions = group.getPermissions();
                    if (permissions.isEmpty()) {
                        sender.sendMessage(fileManager.getMsg("permissions.emptypermissionsingroup").replace("{GROUP}", args[2]));
                        return true;
                    }
                    sender.sendMessage(fileManager.getMsg("permissions.permissioninfomessage").replace("{NAME}", args[2]));
                    for (final String permission : permissions) {
                        sender.sendMessage(fileManager.getMsg("permissions.permissioninfo").replace("{PERMISSION}", permission));
                    }
                } else if (args[1].equalsIgnoreCase("clear")) {
                    try {
                        permissionManager.clear(args[2], false);
                        sender.sendMessage(fileManager.getMsg("permissions.cleargroup").replace("{PLAYER}", args[2]));
                    } catch (final NullPointerException exception) {
                        sender.sendMessage(fileManager.getMsg("cantexist"));
                    } catch (final IOException | GroupHasException | GroupExistException exception) {
                        exception.printStackTrace();
                    }
                } else {
                    printHelp(sender);
                }
                return true;
            } else if (args.length == 2) {
                if (args[1].equalsIgnoreCase("list")) {
                    final List<String> groups = new ArrayList<>(permissionManager.getGroups().keySet());
                    if (groups.isEmpty()) {
                        sender.sendMessage(fileManager.getMsg("permissions.groupsdoesntexist"));
                        return true;
                    }
                    final String groupToList = groups.stream().collect(Collectors.joining(fileManager.getMsg("interlude")));
                    sender.sendMessage(fileManager.getMsg("permissions.list").replace("{GROUPS}", groupToList));
                } else {
                    printHelp(sender);
                }
                return true;
            }
            if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("clone") || args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("priority") || args[1].equalsIgnoreCase("prefix")) {
                if (args.length != 4) {
                    printHelp(sender);
                    return true;
                }
            }
            if (args[1].equalsIgnoreCase("add")) {
                try {
                    permissionManager.addGroup(args[2], args[3]);
                    sender.sendMessage(fileManager.getMsg("permissions.addgroup").replace("{PLAYER}", args[2]));
                } catch (final IOException exception) {
                    exception.printStackTrace();
                } catch (final GroupExistException exception) {
                    sender.sendMessage(fileManager.getMsg("permissions.groupdoesntexist"));
                } catch (final NullPointerException exception) {
                    sender.sendMessage(fileManager.getMsg("cantexist"));
                } catch (final GroupHasException exception) {
                    sender.sendMessage(fileManager.getMsg("permissions.adderror").replace("{PLAYER}", args[2]));
                }
            } else if (args[1].equalsIgnoreCase("remove")) {
                try {
                    permissionManager.removeGroup(args[2], args[3]);
                    sender.sendMessage(fileManager.getMsg("permissions.removegroup").replace("{PLAYER}", args[2]));
                } catch (final IOException exception) {
                    exception.printStackTrace();
                } catch (final GroupExistException exception) {
                    sender.sendMessage(fileManager.getMsg("permissions.groupdoesntexist"));
                } catch (final GroupHasException exception) {
                    sender.sendMessage(fileManager.getMsg("permissions.removeerror").replace("{PLAYER}", args[2]));
                } catch (final NullPointerException exception) {
                    sender.sendMessage(fileManager.getMsg("cantexist"));
                }
            } else if (args[1].equalsIgnoreCase("permission")) {
                if (args.length != 5) {
                    printHelp(sender);
                    return true;
                }
                if (args[2].equalsIgnoreCase("add")) {
                    try {
                        permissionManager.addGroupPermission(args[3], args[4]);
                        sender.sendMessage(fileManager.getMsg("permissions.addgrouppermission").replace("{GROUP}", args[3]).replace("{PERMISSION}", args[4]));
                    } catch (final GroupExistException exception) {
                        sender.sendMessage(fileManager.getMsg("permissions.groupdoesntexist"));
                    }
                } else if (args[2].equalsIgnoreCase("remove")) {
                    try {
                        permissionManager.removeGroupPermission(args[3], args[4]);
                        sender.sendMessage(fileManager.getMsg("permissions.removegrouppermission").replace("{GROUP}", args[3]).replace("{PERMISSION}", args[4]));
                    } catch (final GroupExistException exception) {
                        sender.sendMessage(fileManager.getMsg("permissions.groupdoesntexist"));
                    }
                }
            } else if (args[1].equalsIgnoreCase("clone")) {
                try {
                    permissionManager.cloneGroup(args[2], args[3]);
                    sender.sendMessage(fileManager.getMsg("permissions.clonegroup").replace("{GROUPFROM}", args[2]).replace("{GROUPTO}", args[3]));
                } catch (final GroupExistException exception) {
                    sender.sendMessage(fileManager.getMsg("permissions.groupdoesntexist"));
                }
            } else if (args[1].equalsIgnoreCase("create")) {
                try {
                    permissionManager.createGroup(sender.getName(), args[2], Byte.parseByte(args[3]));
                    sender.sendMessage(fileManager.getMsg("permissions.creategroup").replace("{NAME}", args[2]));
                } catch (final GroupExistException exception) {
                    sender.sendMessage(fileManager.getMsg("permissions.groupexist"));
                }
            } else if (args[1].equalsIgnoreCase("priority")) {
                try {
                    permissionManager.setPriority(args[2], Byte.parseByte(args[3]));
                    sender.sendMessage(fileManager.getMsg("permissions.setpriority").replace("{GROUP}", args[2]).replace("{PRIORITY}", args[3]));
                } catch (final GroupExistException exception) {
                    sender.sendMessage(fileManager.getMsg("permissions.groupdoesntexist"));
                }
            } else if (args[1].equalsIgnoreCase("prefix")) {
                try {
                    permissionManager.setPrefix(args[2], args[3]);
                    sender.sendMessage(fileManager.getMsg("permissions.setprefix").replace("{GROUP}", args[2]).replace("{PREFIX}", args[3]));
                } catch (final GroupExistException exception) {
                    sender.sendMessage(fileManager.getMsg("permissions.groupdoesntexist"));
                }
            } else {
                printHelp(sender);
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("clear")) {
            if (args.length != 2) {
                printHelp(sender);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
            if (args.length != 3) {
                printHelp(sender);
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("info")) {
            if (Bukkit.getPlayer(args[1]) != null) {
                final User user = userManager.getUser(Bukkit.getPlayer(args[1]));
                List<String> permissions = new ArrayList<>(user.getPermissionAttachment().getPermissions().keySet());
                final List<String> msgList = fileManager.getMsgList("permissions.playerinfo");
                final List<String> permissionGroups = user.getPermissionGroups();
                String groupToList = permissionGroups.stream().collect(Collectors.joining(fileManager.getMsg("interlude")));
                if (permissionGroups.isEmpty()) {
                    groupToList = fileManager.getMsg("permissions.emptygroupplayer");
                }
                if (permissions.isEmpty()) {
                    permissions = Collections.singletonList(fileManager.getMsg("permissions.emptypermissionsplayer"));
                }
                for (final String message : msgList) {
                    sender.sendMessage(message.replace("{PLAYER}", args[1]).replace("{GROUPS}", groupToList));
                }
                for (final String permission : permissions) {
                    sender.sendMessage(fileManager.getMsg("permissions.permissioninfo").replace("{PERMISSION}", permission));
                }
                return true;
            }
            try {
                fileManager.getPlayerFile(args[1]);
            } catch (final NullPointerException exception) {
                sender.sendMessage(fileManager.getMsg("cantexist"));
                return true;
            }
            final YamlConfiguration playerFile = fileManager.getPlayerFile(args[1]);
            List<String> permissions = playerFile.getStringList("permissions");
            final List<String> msgList = fileManager.getMsgList("permissions.playerinfo");
            final List<String> groups = playerFile.getStringList("groups");
            String groupToList = groups.stream().collect(Collectors.joining(fileManager.getMsg("interlude")));

            if (groups.isEmpty()) {
                groupToList = fileManager.getMsg("permissions.emptygroupplayer");
            }
            if (permissions.isEmpty()) {
                permissions = Collections.singletonList(fileManager.getMsg("permissions.emptypermissionsplayer"));
            }
            for (final String message : msgList) {
                sender.sendMessage(message.replace("{PLAYER}", args[1]).replace("{GROUPS}", groupToList));
            }
            for (final String permission : permissions) {
                sender.sendMessage(fileManager.getMsg("permissions.prefix").replace("{PERMISSIONS}", permission));
            }
        } else if (args[0].equalsIgnoreCase("clear")) {
            try {
                permissionManager.clear(args[1], true);
                sender.sendMessage(fileManager.getMsg("permissions.clearpermission").replace("{PLAYER}", args[1]));
            } catch (final NullPointerException exception) {
                sender.sendMessage(fileManager.getMsg("cantexist"));
            } catch (final IOException | GroupHasException | GroupExistException exception) {
                exception.printStackTrace();
            }
        } else if (args[0].equalsIgnoreCase("add")) {
            try {
                permissionManager.addPermission(args[1], args[2]);
                sender.sendMessage(fileManager.getMsg("permissions.addpermissionsender").replace("{PERMISSION}", args[2]).replace("{PLAYER}", args[1]));
                Bukkit.getPlayer(args[1]).sendMessage(fileManager.getMsg("permissions.addpermission").replace("{PERMISSION}", args[2]));
            } catch (final IOException exception) {
                exception.printStackTrace();
            } catch (final NullPointerException exception) {
                sender.sendMessage(fileManager.getMsg("cantexist"));
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            try {
                permissionManager.removePermission(args[1], args[2]);
                sender.sendMessage(fileManager.getMsg("permissions.removepermissionsender").replace("{PERMISSION}", args[2]).replace("{PLAYER}", args[1]));
                Bukkit.getPlayer(args[1]).sendMessage(fileManager.getMsg("permissions.removepermission").replace("{PERMISSION}", args[2]));
            } catch (final IOException exception) {
                exception.printStackTrace();
            } catch (final NullPointerException exception) {
                sender.sendMessage(fileManager.getMsg("cantexist"));
            }
        } else {
            printHelp(sender);
        }
        return true;
    }

    private void printHelp(final CommandSender sender) {
        final List<String> msgList = fileManager.getMsgList("permissions.help");
        for (final String message : msgList) {
            sender.sendMessage(message);
        }
    }
}