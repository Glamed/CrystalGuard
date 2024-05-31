package games.sparking.crystalguard;

import games.sparking.crystalguard.reports.commands.ReportCommand;
import games.sparking.crystalguard.staffmode.commands.SpectatorCommand;
import games.sparking.crystalguard.staffmode.commands.VanishCommand;
import games.sparking.crystalguard.staffmode.listeners.VanishListeners;
import games.sparking.crystalguard.menu.listener.MenuListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;

public final class CrystalGuard extends JavaPlugin {

    @Getter
    private static ArrayList<Player> vanished = new ArrayList();

    @Getter
    private static CrystalGuard instance;

    @Override
    public void onEnable() {
        //instance
        instance = this;
        //Menu listener system

        Arrays.asList(
                new VanishListeners(),
                new MenuListener()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, instance));

        getCommand("spectator").setExecutor(new SpectatorCommand());
        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("report").setExecutor(new ReportCommand());


        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : CrystalGuard.getVanished()) {
                    player.sendMessage("");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&5&l✦&8]&7 You are currently vanished &8[&5&l✦&8]"));
                    player.sendMessage("");
                }
            }
        }.runTaskTimer(this, 0L, 1200L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
