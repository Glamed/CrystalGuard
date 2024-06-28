package games.sparking.crystalguard;

import games.sparking.crystalguard.development.DevelopmentListenerCommand;
import games.sparking.crystalguard.punish.commands.PunishCommand;
import games.sparking.crystalguard.reports.Report;
import games.sparking.crystalguard.reports.commands.ChatReportViewCommand;
import games.sparking.crystalguard.reports.commands.ReportCloseCommand;
import games.sparking.crystalguard.reports.commands.ReportCommand;
import games.sparking.crystalguard.reports.commands.ReportHandleCommand;
import games.sparking.crystalguard.reports.listeners.ReportListeners;
import games.sparking.crystalguard.staffmode.StaffMode;
import games.sparking.crystalguard.staffmode.StaffModeVisibilityAdapter;
import games.sparking.crystalguard.staffmode.commands.SpectatorCommand;
import games.sparking.crystalguard.staffmode.commands.StaffModeCommand;
import games.sparking.crystalguard.staffmode.commands.VanishCommand;
import games.sparking.crystalguard.staffmode.listeners.StaffModeListener;
import games.sparking.crystalguard.utils.menu.listener.MenuListener;
import games.sparking.crystalguard.utils.mongo.MongoService;
import games.sparking.crystalguard.visibility.VisibilityService;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class CrystalGuard extends JavaPlugin {

    @Getter
    private boolean staffModeOnJoin = false;

    @Getter
    private static HashMap<UUID, Report> reportsInProgress = new HashMap<>();

    @Getter
    private static CrystalGuard instance;

    @Getter
    private static MongoService mongoService;

    @Override
    public void onEnable() {
        //instance
        instance = this;

        mongoService = new MongoService();
        mongoService.connect();

        //Menu listener system

        List<Listener> events = new ArrayList<>();

        if (Bukkit.getServer().getServerName().equalsIgnoreCase("DEV")) {
            events.add(new DevelopmentListenerCommand());
            getCommand("dev").setExecutor(new DevelopmentListenerCommand());
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        }

        VisibilityService.init();
        VisibilityService.registerVisibilityAdapter(new StaffModeVisibilityAdapter());
        VisibilityService.setOnlineTreatProvider((player, sender) -> {
            if (!(sender instanceof Player))
                return true;

            StaffMode staffMode = StaffMode.get(player);
            if (!staffMode.isVanished())
                return true;

            return sender.hasPermission("cw.staff") || ((Player) sender).canSee(player);
        });


        events.add(new StaffModeListener());
        events.add(new MenuListener());
        events.add(new ReportListeners());

        events.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, instance));

        getCommand("spectator").setExecutor(new SpectatorCommand());
        getCommand("staffmode").setExecutor(new StaffModeCommand());
        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("punish").setExecutor(new PunishCommand());
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("reporthandle").setExecutor(new ReportHandleCommand());
        getCommand("reportclose").setExecutor(new ReportCloseCommand());
        getCommand("chatreportview").setExecutor(new ChatReportViewCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
