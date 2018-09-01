package ru.planet.wgprotect.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Config {
    private static FileConfiguration config = null;

    public Config(FileConfiguration config) {
        this.config = config;
    }

    public static List<String> getBlockedRegions() {
        return (List<String>) config.getList("adminRegion.blockedRegions", new ArrayList());
    }

    public static List<String> getAllowedUsers() {
        return (List<String>) config.getList("adminRegion.allowedUsers", new ArrayList());
    }

    public static List<String> getIgnoredRegions() {
        return (List<String>) config.getList("adminRegion.ignoredRegions", new ArrayList());
    }

    public static String getMessage(String msg) {
        return ChatColor.translateAlternateColorCodes('&', config.getString("messages." + msg, "NONE"));
    }

    public static int getPlayerLimit(Player player) {
        int weLimit = config.getInt("worldEditLimits.defaultLimit", 3000);
        Iterator var3 = player.getEffectivePermissions().iterator();

        while (var3.hasNext()) {
            PermissionAttachmentInfo permissionAttachmentInfo = (PermissionAttachmentInfo) var3.next();
            if (permissionAttachmentInfo.getPermission().startsWith("wgprotect.limit.")) {
                try {
                    int ammount = Integer.parseInt(permissionAttachmentInfo.getPermission().split(".limit.")[1]);
                    weLimit = ammount;
                } catch (NumberFormatException var5) {
                    ;
                }
            }
        }

        return weLimit;
    }

    public static boolean isDebug() {
        return config.getBoolean("debug");
    }
}
