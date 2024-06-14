package games.sparking.crystalguard.development;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import games.sparking.crystalguard.CrystalGuard;
import games.sparking.crystalguard.staffmode.StaffMode;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class DevelopmentListener implements Listener {

    @Getter
    private static final Cache<Player, String> handlers = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .build();


    @EventHandler
    public void onPing(ServerListPingEvent event) {
        event.setMotd(ChatColor.translateAlternateColorCodes('&', "                 &d&l✦ &C&lCRYSTAL WARS &d&l✦ \n              &7" + "Development Server"));
        event.setMaxPlayers(Bukkit.getOnlinePlayers().size() + 1);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        String host = event.getHostname().split("\\.")[0].toLowerCase();
        handlers.put(event.getPlayer(), host);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', "&8[&a+&8]&7 " + player.getName()));
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setOp(true);

        String host = handlers.getIfPresent(player);
        assert host != null;
        if (host.equalsIgnoreCase("staff") && player.hasPermission("cw.staff")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    StaffMode.get(player).toggleEnabled(false);
                }
            }.runTaskLater(CrystalGuard.getInstance(), 20L); // 20 ticks = 1 seconds
        }
        if (host.equalsIgnoreCase("reporthandle") && player.hasPermission("cw.reporthandle")) {

            player.teleport(new Location(Bukkit.getWorld("Staff"), 16.5, 71, -18));
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.performCommand("reporthandle 3");
                }
            }.runTaskLater(CrystalGuard.getInstance(), 20L); // 20 ticks = 1 seconds
        } else {
            player.teleport(new Location(Bukkit.getWorld("world"), 63.5, 71, 101.5, 180, 0));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', "&8[&c-&8]&7 " + event.getPlayer().getName()));
        event.getPlayer().setOp(false);
    }
}
