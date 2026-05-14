package games.sparking.crystalguard.punish;

import lombok.Getter;

@Getter
public enum PunishmentType {
    SUSPENSION("Suspension"),
    CHAT_RESTRICTION("Chat Restriction"),
    DISCORD_RESTRICTION("Discord Restriction"),
    COMP_GAMEPLAY("Competitive Gameplay Restriction"),
    REPORT("Report Restriction"),
    WARN("Friendly Warning");

    private final String name;

    PunishmentType(String name) {
        this.name = name;
    }

}
