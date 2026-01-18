package com.rivals.ranks;

import org.bukkit.ChatColor;

public final class ColorUtil {
    private ColorUtil() {}

    public static String color(String s) {
        if (s == null) return "";
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
