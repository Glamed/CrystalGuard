package games.sparking.crystalguard.reports.listeners;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import games.sparking.crystalguard.CrystalGuard;
import games.sparking.crystalguard.punish.PunishManager;
import games.sparking.crystalguard.punish.PunishmentType;
import games.sparking.crystalguard.reports.MessageCache;
import games.sparking.crystalguard.reports.ReportCategoryType;
import games.sparking.crystalguard.reports.ReportManager;
import games.sparking.crystalguard.reports.ReportService;
import games.sparking.crystalguard.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportListeners implements Listener {

    private static final Gson gson = new Gson();

    private static final Map<String, Double> weights = Map.of(
            "toxicity", 10.0,
            "identity_attack", 15.0,
            "threat", 15.0,           // increased from 7.0 to 15.0
            "severe_toxicity", 15.0,
            "insult", 10.0,
            "obscene", 10.0
    );

    private static final double[] thresholds = {0.2, 0.4, 0.6, 0.7, 0.8};

    private int calculateRisk(Map<String, Double> scores) {
        double sum = 0, total = 0;
        for (var entry : weights.entrySet()) {
            double value = scores.getOrDefault(entry.getKey(), 0.0);
            sum += value * entry.getValue();
            total += entry.getValue();
        }
        double score = total == 0 ? 0 : sum / total;

        // Iterate thresholds from highest to lowest
        for (int i = thresholds.length - 1; i >= 0; i--) {
            if (score >= thresholds[i]) {
                return i + 1;  // Levels are 1-based here
            }
        }
        return 0;
    }

    private List<String> getAccountIds(Collection<? extends Player> players) {
        return players.stream()
                .map(player -> player.getUniqueId().toString())
                .collect(Collectors.toList());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        ReportService.updateStatus(
                CrystalGuard.getReportsInProgress().get(event.getPlayer().getUniqueId()),
                "PENDING",
                event.getPlayer().getUniqueId().toString()
        );
        CrystalGuard.getReportsInProgress().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        String messagePlain = ChatColor.stripColor(event.getMessage());
        int MLFlagLevel = 0;

        try {
            String encodedMsg = URLEncoder.encode(messagePlain, StandardCharsets.UTF_8);
            String apiUrl = "http://localhost:8001/?text=" + encodedMsg;
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            if (connection.getResponseCode() == 200) {
                try (Reader reader = new InputStreamReader(connection.getInputStream())) {
                    Map<String, Object> json = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
                    }.getType());
                    if (json.containsKey("scores")) {
                        @SuppressWarnings("unchecked")
                        Map<String, Double> scores = (Map<String, Double>) json.get("scores");
                        MLFlagLevel = calculateRisk(scores);


                        for (Map.Entry<String, Double> entry : scores.entrySet()) {
                            Bukkit.getLogger().info("  " + entry.getKey() + ": " + entry.getValue());
                        }
                        Bukkit.getLogger().info("Level: " + MLFlagLevel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Or use Bukkit.getLogger().warning(...)
        }

        MessageCache cache = new MessageCache(
                player.getUniqueId().toString(),
                getAccountIds(Bukkit.getOnlinePlayers()),
                System.currentTimeMillis(),
                event.getMessage(),
                MLFlagLevel
        );

        ReportManager.getMessages().put(cache, true);

        if (MLFlagLevel == 4) {
            ReportManager.create(Bukkit.getConsoleSender(), player, ReportCategoryType.CHAT_ABUSE);
        }
        if (MLFlagLevel == 5) {
            event.setCancelled(true);
            new PunishManager(Bukkit.getConsoleSender(), player, PunishmentType.CHAT_RESTRICTION, TimeUtils.parseTime("1d"), games.sparking.crystalguard.punish.InfractionType.TEMP_AUTOMATED, event.getMessage()).issue();
        }
    }
}
