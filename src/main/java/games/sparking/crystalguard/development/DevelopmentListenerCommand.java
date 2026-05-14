package games.sparking.crystalguard.development;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import games.sparking.crystalguard.CrystalGuard;
import games.sparking.crystalguard.staffmode.StaffMode;
import games.sparking.crystalguard.utils.messages.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class DevelopmentListenerCommand implements Listener, CommandExecutor {

    @Getter
    private static final Cache<Player, String> handlers = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .build();

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        String host = event.getHostname().split("\\.")[0].toLowerCase();
        handlers.put(event.getPlayer(), host);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().getName().equalsIgnoreCase("Glamify")) {
            event.setFormat(CC.format("&8[&4Staff&8]&7 %s &8&l»&f %s", event.getPlayer().getName(), event.getMessage()));
        } else {
            event.setFormat(CC.format("&8[&4SIT&8]&7 %s &8&l»&f %s", event.getPlayer().getName(), event.getMessage()));
        }
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
        } else if (host.equalsIgnoreCase("reporthandle") && player.hasPermission("cw.staff")) {

            player.teleport(new Location(Bukkit.getWorld("Staff"), 16.5, 71, -18));
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.performCommand("reporthandle 3");
                }
            }.runTaskLater(CrystalGuard.getInstance(), 20L); // 20 ticks = 1 seconds
        } else {
            player.teleport(new Location(Bukkit.getWorld("world"), 2610, 156, 308, 180, 0));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', "&8[&c-&8]&7 " + event.getPlayer().getName()));
        event.getPlayer().setOp(false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        Player player = (Player) sender;

        if (alias.equalsIgnoreCase("gm")) {
            if (player.getGameMode() == GameMode.CREATIVE) {
                player.sendMessage(CC.format("&5&l✦ &7Toggled your creative: " + CC.colorBoolean(false, true)));
                player.setGameMode(GameMode.SURVIVAL);
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
            } else {
                player.sendMessage(CC.format("&5&l✦ &7Toggled your creative: " + CC.colorBoolean(true, true)));
                player.setGameMode(GameMode.CREATIVE);
            }
        } else if (alias.equalsIgnoreCase("hub") || alias.equalsIgnoreCase("lobby")) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("Lobby-1");
            player.sendPluginMessage(CrystalGuard.getInstance(), "BungeeCord", out.toByteArray());
        }
        return true;
    }
}
