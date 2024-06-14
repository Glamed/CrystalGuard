package games.sparking.crystalguard.reports.listeners;

import games.sparking.crystalguard.reports.MessageCache;
import games.sparking.crystalguard.reports.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReportMessageListener implements Listener {

    public List<UUID> getAccountIds(Collection<? extends Player> players) {
        return players.stream().map(Player::getUniqueId).collect(Collectors.toList());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void thingthongthanggirlypop(AsyncPlayerChatEvent event) {
        ReportManager.getMessages().put(new MessageCache(event.getPlayer().getUniqueId().toString(), getAccountIds(Bukkit.getOnlinePlayers()), System.currentTimeMillis(), event.getMessage()), true);

    }

}
