package games.sparking.crystalguard.punish;

import games.sparking.crystalguard.reports.PunishmentTypes;
import games.sparking.crystalguard.utils.CC;
import games.sparking.crystalguard.utils.TimeUtils;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PunishManager {

    private Player player;
    private Player staff;
    private PunishmentType type;
    private long duration;
    private PunishmentTypes reason;
    private String message;

    public PunishManager(Player player, Player staff, PunishmentType type, long duration, PunishmentTypes reason) {
        this.player = player;
        this.staff = staff;
        this.type = type;
        this.duration = duration;
        this.reason = reason;
    }

    public void issue() {
        if (type == PunishmentType.BAN) {

            player.kickPlayer(ChatColor.translateAlternateColorCodes('&',
                    "&5Your account has been suspended from Crystal Wars" +
                            "\n" +
                            "&7" +
                            "\"" +
                            reason.getName() +
                            "&7" +
                            "\"" +
                            "\n" +
                            "\n" +
                            "&7This suspension will never expire, vist &d&ncrystalwars.net/appeal&r&7 to submit an appeal"));

        }

        if (type == PunishmentType.MUTE) {
            player.sendMessage(CC.format("&8&m------------------------------------------------"));
            player.sendMessage(CC.format(" &7Your recent activity broke the Crystal Wars rules"));
            if (message != null) {
                player.sendMessage(CC.format("  &8&l→ &5&lDEV&7 Glamify&8:&7 How are you??"));
                player.sendMessage("");
            }
            player.sendMessage("");
            player.sendMessage(CC.format(" &7We took these actions&8:"));

            if (message != null) {
                player.sendMessage(CC.format("  &4\u2715&c This content has been removed so no one can see it."));
            }

            if (duration == -1) {
                player.sendMessage(CC.format("  &4\u2715&c You cannot send minecraft messages permanently."));
                player.sendMessage(CC.format("  &4\u2715&c You cannot send discord messages permanently."));
            } else {
                player.sendMessage(CC.format("  &4\u2715&c You cannot send minecraft messages for " + TimeUtils.formatDetailed(0) + "."));
                player.sendMessage(CC.format("  &4\u2715&c You cannot send discord messages for " + TimeUtils.formatDetailed(0) + "."));
            }

            player.sendMessage("");
            player.sendMessage(CC.format(" &7Why we took these actions&8:"));
            player.sendMessage(CC.format("  &7Our trust and safety team uses automation and manual"));
            player.sendMessage(CC.format("  &7review to enforce our rules. We believe that you have"));
            player.sendMessage(CC.format("  &7violated our community guidelines on %s.", reason));
            player.sendMessage("");
            player.sendMessage(CC.format(" &7Please review our &b&nCommunity Guidelinees&7."));
            player.sendMessage(CC.format(" &7Did we make a mistake? &b&nLet us know&7!"));

            player.sendMessage(CC.format("&8&m------------------------------------------------"));
        }

        if (type == PunishmentType.WARN) {
            player.sendMessage(CC.format("&8&m------------------------------------------------"));
            player.sendMessage(CC.format(" &7Your recent activity broke the Crystal Wars rules"));
            if (message != null) {
                player.sendMessage(CC.format("  &8&l→ &5&lDEV&7 Glamify&8:&7 How are you??"));
                player.sendMessage("");
            }

            player.sendMessage("");
            player.sendMessage(CC.format(" &7We took these actions&8:"));
            player.sendMessage(CC.format("  &4\u2715&c Friendly warning. please correct future behavior."));

            player.sendMessage("");
            player.sendMessage(CC.format(" &7Why we took these actions&8:"));
            player.sendMessage(CC.format("  &7Our trust and safety team uses automation and manual"));
            player.sendMessage(CC.format("  &7review to enforce our rules. We believe that you have"));
            player.sendMessage(CC.format("  &7violated our community guidelines on %s.", reason));
            player.sendMessage("");
            player.sendMessage(CC.format(" &7Please review our &b&nCommunity Guidelinees&7."));
            player.sendMessage(CC.format(" &7Did we make a mistake? &b&nLet us know&7!"));

            player.sendMessage(CC.format("&8&m------------------------------------------------"));
        }
    }

}
