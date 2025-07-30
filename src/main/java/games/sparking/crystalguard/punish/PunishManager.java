package games.sparking.crystalguard.punish;

import games.sparking.crystalguard.reports.PunishmentTypes;
import games.sparking.crystalguard.utils.CC;
import games.sparking.crystalguard.utils.TimeUtils;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PunishManager {

    private final Player player;
    private final Player staff;
    private final PunishmentType type;
    private final long duration;
    private final PunishmentTypes reason;
    private final String message;

    public PunishManager(Player player, Player staff, PunishmentType type, long duration, PunishmentTypes reason) {
        this(player, staff, type, duration, reason, null);
    }

    public void issue() {
        switch (type) {
            case BAN -> handleBan();
            case MUTE -> handleMute();
            case WARN -> handleWarn();
        }
    }

    private void handleBan() {
        String reasonText = reason.getName();
        boolean permanent = duration == -1;

        String banMessage = "&5Your account has been suspended"
                + "\n&7\"" + reasonText + "&7\""
                + "\n\n&7This suspension " + (permanent
                ? "will never expire"
                : "will expire in &d" + TimeUtils.formatDetailed(duration) + "&7")
                + ". Visit &d&ncrystalwars.net/appeal&r&7 to submit an appeal";

        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', banMessage));
    }


    private void handleMute() {
        sendHeader();
        player.sendMessage(CC.format(" &7Your recent activity violated our Terms of Service"));

        if (message != null) {
            displayMessageContent();
        }

        player.sendMessage("");
        player.sendMessage(CC.format(" &7We took these actions&8:"));

        if (message != null) {
            player.sendMessage(CC.format("  &4\u2715&c This content has been removed so no one can see it."));
        }

        if (duration == -1) {
            player.sendMessage(CC.format("  &4\u2715&c You cannot send Minecraft messages permanently."));
            player.sendMessage(CC.format("  &4\u2715&c You cannot send Discord messages permanently."));
        } else {
            String time = TimeUtils.formatDetailed(duration);
            player.sendMessage(CC.format("  &4\u2715&c You cannot send Minecraft messages for " + time + "."));
            player.sendMessage(CC.format("  &4\u2715&c You cannot send Discord messages for " + time + "."));
        }

        sendReasonSection();
        sendHeader();
    }

    private void handleWarn() {
        sendHeader();
        player.sendMessage(CC.format(" &7Your recent activity broke our Terms of Service"));

        if (message != null) {
            displayMessageContent();
        }

        player.sendMessage("");
        player.sendMessage(CC.format(" &7We took these actions&8:"));
        player.sendMessage(CC.format("  &4\u2715&c Friendly warning. Please correct future behavior."));

        sendReasonSection();
        sendHeader();
    }

    private void sendHeader() {
        player.sendMessage(CC.format("&8&m------------------------------------------------"));
    }


    private void sendReasonSection() {
        player.sendMessage("");
        player.sendMessage(CC.format(" &7Why we took these actions&8:"));
        player.sendMessage(CC.format("  &7Our trust and safety team uses automation and manual"));
        player.sendMessage(CC.format("  &7review to enforce our rules. We believe that you have"));
        player.sendMessage(CC.format("  &7violated our community guidelines on &d%s&7.", reason.getName()));
        player.sendMessage("");
        player.sendMessage(CC.format(" &7Please review our &b&nCommunity Guidelines&7."));
        player.sendMessage(CC.format(" &7Did we make a mistake? &b&nLet us know&7!"));
    }

    private void displayMessageContent() {
        player.sendMessage(CC.format(""));
        player.sendMessage(CC.format("  &8&l→ &5&lDEV&7 Glamify&8:&7 " + message));
        player.sendMessage("");
    }
}
