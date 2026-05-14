package games.sparking.crystalguard.utils.messages;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

public class CC {
    // Basic Colors
    public static final String MINECRAFT_BLACK = ChatColor.BLACK.toString();
    public static final String MINECRAFT_DARK_BLUE = ChatColor.DARK_BLUE.toString();
    public static final String MINECRAFT_DARK_GREEN = ChatColor.DARK_GREEN.toString();
    public static final String MINECRAFT_DARK_AQUA = ChatColor.DARK_AQUA.toString();
    public static final String MINECRAFT_DARK_RED = ChatColor.DARK_RED.toString();
    public static final String MINECRAFT_DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
    public static final String MINECRAFT_DARK_GRAY = ChatColor.DARK_GRAY.toString();
    public static final String MINECRAFT_BLUE = ChatColor.BLUE.toString();
    public static final String MINECRAFT_GREEN = ChatColor.GREEN.toString();
    public static final String MINECRAFT_AQUA = ChatColor.AQUA.toString();
    public static final String MINECRAFT_RED = ChatColor.RED.toString();
    public static final String MINECRAFT_LIGHT_PURPLE = ChatColor.LIGHT_PURPLE.toString();
    public static final String MINECRAFT_YELLOW = ChatColor.YELLOW.toString();
    public static final String MINECRAFT_WHITE = ChatColor.WHITE.toString();
    public static final String MINECRAFT_GRAY = ChatColor.GRAY.toString();
    public static final String MINECRAFT_GOLD = ChatColor.GOLD.toString();

    // Hex Colors
    public static final String HEX_RED = ChatColor.of("#000000").toString();

    // Formatting
    public static final String BOLD = ChatColor.BOLD.toString();
    public static final String ITALIC = ChatColor.ITALIC.toString();
    public static final String UNDERLINE = ChatColor.UNDERLINE.toString();
    public static final String STRIKETHROUGH = ChatColor.STRIKETHROUGH.toString();
    public static final String RESET = ChatColor.RESET.toString();
    public static final String MAGIC = ChatColor.MAGIC.toString();

    // Symbols
    public static final String CROWN = MINECRAFT_DARK_RED + StringEscapeUtils.unescapeJava("♛");
    public static final String LEFT_ARROW = StringEscapeUtils.unescapeJava("«");
    public static final String RIGHT_ARROW = StringEscapeUtils.unescapeJava("»");
    public static final String VERTICAL_BAR = StringEscapeUtils.unescapeJava("┃");
    public static final String X = StringEscapeUtils.unescapeJava("✕");
    public static final String CHECK_MARK = StringEscapeUtils.unescapeJava("✔");

    // Bars
    public static final String MENU_BAR = MINECRAFT_GRAY + STRIKETHROUGH + "------------------------";
    public static final String CHAT_BAR = MINECRAFT_GRAY + STRIKETHROUGH + "------------------------------------------------";
    public static final String SMALL_CHAT_BAR = MINECRAFT_GRAY + STRIKETHROUGH + "-----------------";
    public static final String SB_BAR = MINECRAFT_GRAY + STRIKETHROUGH + "----------------------";

    // Colorize a string using '&' codes
    public static String translate(String in) {
        return ChatColor.translateAlternateColorCodes('&', in == null ? "" : in);
    }

    // Colorize a list of strings
    public static List<String> translate(List<String> lines) {
        List<String> toReturn = new ArrayList<>();
        for (String line : lines) {
            toReturn.add(translate(line));
        }
        return toReturn;
    }

    // Colorize an array of strings
    public static List<String> translate(String[] lines) {
        List<String> toReturn = new ArrayList<>();
        for (String line : lines) {
            if (line != null) {
                toReturn.add(translate(line));
            }
        }
        return toReturn;
    }

    // Format a string using String.format, then colorize
    public static String format(String in, Object... args) {
        return translate(String.format(in, args));
    }

    // Format a list of strings
    public static List<String> format(List<String> lines, Object... args) {
        List<String> toReturn = new ArrayList<>();
        for (String line : lines) {
            toReturn.add(format(line, args));
        }
        return toReturn;
    }

    // Boolean → colored "Enabled"/"Disabled"
    public static String colorBoolean(boolean b) {
        return colorBoolean(b, false);
    }

    // Boolean with capitalized option
    public static String colorBoolean(boolean b, boolean capitalize) {
        return colorBoolean(b, (capitalize ? "E" : "e") + "nabled", (capitalize ? "D" : "d") + "isabled");
    }

    // Boolean with custom text
    public static String colorBoolean(boolean b, String enabled, String disabled) {
        return b ? MINECRAFT_GREEN + enabled : MINECRAFT_RED + disabled;
    }

    // Replace markdown-style asterisks with formatting
    public static String replaceMarkdown(String s) {
        return s == null ? "" : s.replaceAll("\\*(.+?)\\*", "&f$1&7");
    }

    // Simple permission error message

    public static String permMsg(String rank) {
        if (rank == null || rank.isEmpty()) {
            return MessageType.ERROR.format("Unknown rank.");
        }

        // Staff-type ranks
        if (rank.equalsIgnoreCase("staff") || rank.equalsIgnoreCase("associate")) {
            return errorMsg(Messages.INVALID);
        }

        // Specific rank checks
        if (rank.equalsIgnoreCase("prime")) {
            return MessageType.ERROR.format("You must be a *Prime* (" + CROWN + ") member for this. Use /plus.");
        }

        if (rank.contains("donor")) {
            return MessageType.ERROR.format("This is a donor feature. Type /donate for access!");
        }

        if (rank.contains("premium")) {
            return MessageType.ERROR.format("This is a Premium feature. Type /donate for access!");
        }

        if (rank.contains("elite")) {
            return MessageType.ERROR.format("This is an Elite feature. Type /donate for access!");
        }

        if (rank.contains("vip")) {
            return MessageType.ERROR.format("This is a VIP feature. Type /donate for access!");
        }

        if (rank.contains("supporter")) {
            return MessageType.ERROR.format("This is a Supporter feature. Type /donate for access!");
        }

        return MessageType.ERROR.format("You do not have permission to use this.");
    }

    // Helper for error-styled messages (using external Messages enum)
    public static String errorMsg(Messages msg, Object... args) {
        return MessageType.ERROR.format(msg, args);
    }

    public static String noticeMsg(Messages msg, Object... args) {
        return MessageType.NOTICE.format(msg, args);
    }

    public static String successMsg(Messages msg, Object... args) {
        return MessageType.SUCCESS.format(msg, args);
    }
}