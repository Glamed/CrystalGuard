package games.sparking.crystalguard.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Profile {

    public static List<Player> getPlayersAlphabetically() {
        List<Player> onlinePlayers = Bukkit.getOnlinePlayers().stream()
                .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                .collect(Collectors.toList());
        return onlinePlayers;
    }
}
