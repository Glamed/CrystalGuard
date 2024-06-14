package games.sparking.crystalguard.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class CC {
    
    public static String translate(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static List<String> translate(List<String> lines) {
        List<String> toReturn = new ArrayList();
        for (String line : lines) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return toReturn;
    }

    public static List<String> translate(String[] lines) {
        List<String> toReturn = new ArrayList();
        for (String line : lines) {
            if (line != null) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }
        return toReturn;
    }

    public static String strip(String in) {
        return ChatColor.stripColor(in);
    }

    public static String format(String in, Object... args) {
        return String.format(translate(in), args);
    }

    public static List<String> format(List<String> lines, Object... args) {
        List<String> toReturn = new ArrayList<>();
        for (String line : lines) {
            toReturn.add(String.format(translate(line), args));
        }
        return toReturn;
    }

    public static String colorBoolean(boolean b) {
        return colorBoolean(b, false);
    }

    public static String colorBoolean(boolean b, boolean capitalize) {
        return colorBoolean(b, (capitalize ? "E" : "e") + "nabled", (capitalize ? "D" : "d") + "isabled");
    }

    public static String colorBoolean(boolean b, String enabled, String disabled) {
        return b ? ChatColor.GREEN + enabled : org.bukkit.ChatColor.RED + disabled;
    }
}
