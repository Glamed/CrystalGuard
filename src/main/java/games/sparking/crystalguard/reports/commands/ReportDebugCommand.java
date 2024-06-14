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

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class ReportDebugCommand implements CommandExecutor {

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

        if (args[0].equalsIgnoreCase("0")) {
            sender.sendMessage(CrystalGuard.getReports().size() + " reports pending");

            return true;
        }

        if (args[0].equalsIgnoreCase("1")) {
            TreeMap<Double, Report> reports = new TreeMap<>();

            // Populate the reports TreeMap
            for (Report report : CrystalGuard.getReports()) {
                double priority = 0;

                for (Reason reason : report.getReasons()) {
                    priority += 5;

                    double ageImpact = priority * Math.pow(0.95, reason.getTimeElapsedSinceReport().toMinutes());

                    priority += reason.getMessage().getPriority() * ageImpact;
                }

                reports.put(priority, report);
            }

            // Send reports in order of priority
            for (Map.Entry<Double, Report> entry : reports.entrySet()) {
                double priority = entry.getKey();
                Report report = entry.getValue();
                sender.sendMessage(priority + ", " + report.getReportID());
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("2")) {
            sender.sendMessage(getNextReport().getReportID());
            return true;
        }

        if (args[0].equalsIgnoreCase("3")) {
            Report report = getNextReport();
            sender.sendMessage(CC.format("&8&m------------------------------------------------"));
            sender.sendMessage("Report ID: " + report.getReportID());
            sender.sendMessage("Suspect: " + Bukkit.getPlayer(UUID.fromString(report.getSuspectUUID())).getName());
            sender.sendMessage("Category: " + report.getCategory());
            sender.sendMessage(report.getReasons().size() + " total reports: ");
            int size = 1;
            for (Reason reason : report.getReasons()) {
                sender.sendMessage("(" + size++ + ") " + Bukkit.getPlayer(UUID.fromString(reason.getUuid())).getName() + ": " + reason.getMessage().getName());
            }
            sender.sendMessage(CC.format("&8&m------------------------------------------------"));
            return true;
        }

        if (args[0].equalsIgnoreCase("4")) {
            int sizee = 1;
            for (Report report : CrystalGuard.getReports()) {
                sender.sendMessage(ChatColor.RED + " " + sizee + " | " + CrystalGuard.getReports().size());
                sender.sendMessage(CC.format("&8&m------------------------------------------------"));
                sender.sendMessage("Report ID: " + report.getReportID());
                sender.sendMessage("Suspect: " + Bukkit.getPlayer(UUID.fromString(report.getSuspectUUID())).getName());
                sender.sendMessage("Category: " + report.getCategory());
                sender.sendMessage(report.getReasons().size() + " total reports: ");
                int size = 1;
                for (Reason reason : report.getReasons()) {
                    sender.sendMessage("(" + size++ + ") " + Bukkit.getPlayer(UUID.fromString(reason.getUuid())).getName() + ": " + reason.getMessage().getName());
                }
                if (report.getMessages() != null && report.getMessages().size() > 0) {
                    System.out.println("Report ID: " + report.getReportID());
                }
                sender.sendMessage(CC.format("&8&m------------------------------------------------"));
                sizee++;
            }
            return true;
        }
        return true;
    }
}
