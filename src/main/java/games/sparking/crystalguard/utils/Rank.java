package games.sparking.crystalguard.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rank {

    OWNER("&5&lOWNER"),
    STAFF("&5&lSTAFF"),
    DEV("&5&lDEV");

    private String prefix;
}
