package games.sparking.crystalguard.punish;

import games.sparking.crystalguard.utils.TimeUtils;
import games.sparking.crystalguard.utils.messages.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@AllArgsConstructor
public class PunishManager {

    private final UUID playerUUID;
    private final UUID staffUUID;
    private final PunishmentType type;
    private final long duration;
    private final InfractionType reason;
    private final String message;

    // Updated constructor for use from commands
    public PunishManager(CommandSender staff, Player target, PunishmentType type, long duration, InfractionType reason) {
        this(staff, target, type, duration, reason, null);
    }

    public PunishManager(CommandSender staff, Player target, PunishmentType type, long duration, InfractionType reason, String message) {
        this.playerUUID = target.getUniqueId();
        this.staffUUID = (staff instanceof Player player) ? player.getUniqueId() : UUID.fromString("63644fed-6a20-4c35-bef4-be5e1d785a2e");
        this.type = type;
        this.duration = duration;
        this.reason = reason;
        this.message = message;
    }

    public void issue() {
        switch (type) {
            case SUSPENSION -> handleBan();
            case CHAT_RESTRICTION -> handleMute();
            case WARN -> handleWarn();
        }
    }

    private void handleBan() {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        String reasonText = reason.getDisplayName();
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
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        sendHeader(player);
        player.sendMessage(CC.format(" &7Your recent activity violated our Terms of Service"));

        if (message != null) {
            displayMessageContent(player);
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

        sendReasonSection(player);
        sendHeader(player);
    }

    private void handleWarn() {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        sendHeader(player);
        player.sendMessage(CC.format(" &7Your recent activity broke our Terms of Service"));

        if (message != null) {
            displayMessageContent(player);
        }

        player.sendMessage("");
        player.sendMessage(CC.format(" &7We took these actions&8:"));
        player.sendMessage(CC.format("  &4\u2715&c Friendly warning. Please correct future behavior."));

        sendReasonSection(player);
        sendHeader(player);
    }

    private void sendHeader(Player player) {
        player.sendMessage(CC.format("&8&m------------------------------------------------"));
    }

    private void sendReasonSection(Player player) {
        player.sendMessage("");

        if (reason == InfractionType.TEMP_AUTOMATED) {
            player.sendMessage(CC.format(" &7Why this action was taken&8:"));
            player.sendMessage(CC.format("  &7This temporary action was triggered by our"));
            player.sendMessage(CC.format("  &7automated moderation systems and is pending"));
            player.sendMessage(CC.format("  &7manual review by our Trust & Safety team."));
            player.sendMessage("");
            player.sendMessage(CC.format(" &7You cannot appeal this action at this time."));
            player.sendMessage(CC.format(" &7Please review our &b&nCommunity Guidelines&7 while we complete our review."));
        } else {
            player.sendMessage(CC.format(" &7Why we took these actions&8:"));
            player.sendMessage(CC.format("  &7Our trust and safety team uses automation and manual"));
            player.sendMessage(CC.format("  &7review to enforce our rules. We believe that you have"));
            player.sendMessage(CC.format("  &7violated our community guidelines on &d%s&7.", reason.getDisplayName()));
            player.sendMessage("");
            player.sendMessage(CC.format(" &7Please review our &b&nCommunity Guidelines&7."));
            player.sendMessage(CC.format(" &7Did we make a mistake? &b&nLet us know&7!"));
        }
    }

    private void displayMessageContent(Player player) {
        player.sendMessage(CC.format(""));
        player.sendMessage(CC.format("  &8&l→ &8[&7Member&8]&7 " + player.getName() + " &8»&f " + message));
        player.sendMessage("");
    }
}
