package games.sparking.crystalguard.reports.listeners;

import games.sparking.crystalguard.CrystalGuard;
import games.sparking.crystalguard.reports.MessageCache;
import games.sparking.crystalguard.reports.ReportManager;
import games.sparking.crystalguard.reports.ReportService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReportListeners implements Listener {

    public List<String> getAccountIds(Collection<? extends Player> players) {
        return players.stream()
                .map(player -> player.getUniqueId().toString()) // Convert UUID to String
                .collect(Collectors.toList());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void quit(PlayerQuitEvent event) {
        ReportService.updateStatus(CrystalGuard.getReportsInProgress().get(event.getPlayer().getUniqueId()), "PENDING", event.getPlayer().getUniqueId().toString());
        CrystalGuard.getReportsInProgress().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void thingthongthanggirlypop(AsyncPlayerChatEvent event) {
        ReportManager.getMessages().put(new MessageCache(event.getPlayer().getUniqueId().toString(), getAccountIds(Bukkit.getOnlinePlayers()), System.currentTimeMillis(), event.getMessage()), true);

    }

}
