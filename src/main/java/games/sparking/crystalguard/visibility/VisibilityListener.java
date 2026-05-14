package games.sparking.crystalguard.visibility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class VisibilityListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        VisibilityService.update(event.getPlayer());
        ArrayList<Player> viewers = new ArrayList<>();
        ArrayList<String> playerNames = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("cw.staff")) {
                viewers.add(player);
                playerNames.add(player.getName()); // or player.getUniqueId().toString() if you use UUIDs
            }
        }

        VisibilityService.teamUpdate(viewers, playerNames);

    }


}
