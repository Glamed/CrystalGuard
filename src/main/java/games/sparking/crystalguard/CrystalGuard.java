package games.sparking.crystalguard;

import games.sparking.crystalguard.commands.CommandListener;
import games.sparking.crystalguard.development.DevelopmentListenerCommand;
import games.sparking.crystalguard.punish.commands.PunishCommand;
import games.sparking.crystalguard.reports.Report;
import games.sparking.crystalguard.reports.commands.*;
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
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class CrystalGuard extends JavaPlugin {

    @Getter
    private boolean staffModeOnJoin = false;

    @Getter
    private static HashMap<UUID, Report> reportsInProgress = new HashMap<>();

    @Getter
    private static CrystalGuard instance;

    @Getter
    private static MongoService mongoService;

    @Getter
    private static boolean devMode = false;

    @Override
    public void onEnable() {
        //instance
        instance = this;

        registerConfig();

        mongoService = new MongoService();
        mongoService.connect();

        //Menu listener system


        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        VisibilityService.init();
        VisibilityService.registerVisibilityAdapter(new StaffModeVisibilityAdapter());
        VisibilityService.setOnlineTreatProvider((player, sender) -> {
            if (!(sender instanceof Player))
                return true;

            StaffMode staffMode = StaffMode.get(player);
            if (!staffMode.isVanished())
                return true;

            return sender.hasPermission("cg.staff") || ((Player) sender).canSee(player);
        });

        registerCommands();
        registerEvents();

    }

    public void registerConfig() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
    }


    public void registerCommands() {
        Map<String, CommandExecutor> commands = new HashMap<>();

        commands.put("spectator", new SpectatorCommand());
        commands.put("staffmode", new StaffModeCommand());
        commands.put("vanish", new VanishCommand());
        commands.put("punish", new PunishCommand());
        commands.put("report", new ReportCommand());
        commands.put("reporthandle", new ReportHandleCommand());
        commands.put("reportclose", new ReportCloseCommand());
        commands.put("chatreportview", new ChatReportViewCommand());
        commands.put("ReportDebug", new ReportDebugCommand());

        if (devMode) {
            commands.put("dev", new DevelopmentListenerCommand());
        }

        commands.forEach((key, value) -> Objects.requireNonNull(getCommand(key)).setExecutor(value));
    }

    public void registerEvents() {
        List<Listener> events = new ArrayList<>();

        events.add(new StaffModeListener());
        events.add(new MenuListener());
        events.add(new ReportListeners());
        events.add(new CommandListener());

        if (devMode) {
            events.add(new DevelopmentListenerCommand());
        }

        events.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, instance));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
