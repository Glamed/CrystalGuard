package games.sparking.crystalguard.utils.messages;

import lombok.Getter;

@Getter
public enum Messages {

    DISABLED("System disabled.", "This system is currently disabled."),
    VANISHED("System disabled.", "You cannot use this system while vanished."),
    UNVANISHED("System disabled.", "You cannot use this system while not vanished."),
    CONNECTED("Invalid player.", "That player has never been on Sparking before."),
    OFFLINE("Invalid player.", "This player is offline or on a different realm."),
    REBOOT("System disabled.", "This system is disabled before a reboot."),
    CONSOLE("Invalid instance.", "This command is not available from the console."),
    WORLD("Invalid world", "This command is not available on this server."),
    SERVER("Invalid server", "This command is disabled before a reboot."),
    COOLDOWN("Cooldown active.", "*%s* remaining"),
    SYNTAX("Invalid Syntax.", "Try *%s*."),
    INVALID("Unknown Command.", "&7Try &f/help&7 for help."),
    PERMISSION("No Permission.", "You don't have permission to use this command.");


    private final String reason;
    private final String main;

    Messages(String reason, String main) {
        this.reason = reason;
        this.main = main;
    }

}
