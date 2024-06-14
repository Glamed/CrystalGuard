package games.sparking.crystalguard.reports.commands;

import games.sparking.crystalguard.CrystalGuard;
import games.sparking.crystalguard.reports.Reason;
import games.sparking.crystalguard.reports.Report;
import games.sparking.crystalguard.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.TreeMap;
import java.util.UUID;

public class ReportHandleCommand implements CommandExecutor {

    public Report getNextReport() {
        TreeMap<Double, Report> reports = new TreeMap<>();

        for (Report report : CrystalGuard.getReports()) {
            double priority = 30;

            for (Reason reason : report.getReasons()) {
                double ageImpact = Math.pow(0.95, reason.getTimeElapsedSinceReport().toMinutes()); // Calculate age impact separately

                priority += 5 * ageImpact; // Add 5 to priority before multiplying by age impact
                priority += reason.getMessage().getPriority() * ageImpact;
            }

            reports.put(priority, report);
        }

        return reports.isEmpty() ? null : reports.firstEntry().getValue(); // Get the report with the lowest priority
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Jake you're probably retarded.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("cw.reporthandle")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&l✦ &7This command was not recognized&5."));
            return true;
        }

        if (CrystalGuard.getReportsInProgress().containsKey(player.getUniqueId())) {
            Report report = CrystalGuard.getReports().get(0);

            displayReport(player, report);
            return true;
        }

        Report report = getNextReport();

        if (report == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&l✦ &7There are no open reports, awesome work!"));
            return true;
        }

        CrystalGuard.getReportsInProgress().put(player.getUniqueId(), report.getReportID());

        displayReport(player, report);

        return true;
    }


    public void displayReport(Player player, Report report) {
        player.sendMessage(CC.format("&8&m------------------------------------------------"));
        if (report.getMessages() == null) {
            player.sendMessage(CC.format(" &7Report ID&8: &d" + report.getReportID()));
        } else {
            player.sendMessage(CC.format(" &7Report ID&8: &d" + report.getReportID() + " &8[&5Chat History&8]"));
        }
        player.sendMessage(CC.format(" &7Suspect&8: &d" + Bukkit.getPlayer(UUID.fromString(report.getSuspectUUID())).getName()));
        player.sendMessage(CC.format(" &7Category&8: &d" + report.getCategory()));
        player.sendMessage(CC.format(" &7" + report.getReasons().size() + " total report" + (report.getReasons().size() > 1 ? "" : "s") + "&8: "));
        int size = 1;
        for (Reason reason : report.getReasons()) {
            player.sendMessage(CC.format("  &8-&7 " + size++ + "&8: &5" + Bukkit.getPlayer(UUID.fromString(reason.getUuid())).getName() + ": &d" + reason.getMessage().getName()));
        }
        player.sendMessage(CC.format("&8&m------------------------------------------------"));
    }
}
