package games.sparking.crystalguard.utils.messages;

public enum MessageType {
    ERROR("&4" + CC.X, "&c"),
    NOTICE("&3&l" + CC.VERTICAL_BAR, "&b"),
    SUCCESS("&2" + CC.CHECK_MARK, "&a");

    private final String prefix;
    private final String messageColor;

    MessageType(String prefix, String messageColor) {
        this.prefix = prefix;
        this.messageColor = messageColor;
    }

    public String format(String reason, String main, Object... args) {
        boolean hasReason = reason != null && !reason.isEmpty();
        boolean hasMain = main != null && !main.isEmpty();

        StringBuilder builder = new StringBuilder(prefix + " ");

        if (hasReason) builder.append(messageColor).append(CC.replaceMarkdown(reason));
        if (hasMain) {
            if (hasReason) builder.append(" &7");
            else builder.append("&7");
            builder.append(CC.replaceMarkdown(main));
        }

        return CC.format(builder.toString(), args);
    }

    public String format(Messages messages, Object... args) {
        return format(messages.getReason(), messages.getMain(), args);
    }

    public String format(String reason, Object... args) {
        return format(reason, null, args);
    }
}