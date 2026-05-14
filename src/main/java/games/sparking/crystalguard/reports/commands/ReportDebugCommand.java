package games.sparking.crystalguard.reports.commands;

import games.sparking.crystalguard.reports.Reason;
import games.sparking.crystalguard.reports.Report;
import games.sparking.crystalguard.reports.ReportCategoryType;
import games.sparking.crystalguard.reports.ReportService;
import games.sparking.crystalguard.utils.messages.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class ReportDebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Jake you're probably retarded.");
            return true;
        }

        if (!sender.hasPermission("cw.staff")) {
            sender.sendMessage(CC.format("&5&l✦ &7You don't have permission to use this command."));
            return true;
        }

        if (args[0].equalsIgnoreCase("0")) {
            sender.sendMessage(((Collection<?>) ReportService.getPending()).size() + " reports pending");

            return true;
        }

        if (args[0].equalsIgnoreCase("1")) {
            TreeMap<Double, Report> reports = new TreeMap<>();

            // Populate the reports TreeMap
            for (Report report : ReportService.getPending()) {
                double priority = 0;

                for (Reason reason : report.getReasons()) {
                    priority += 5;

                    double ageImpact = priority * Math.pow(0.95, reason._getTimeElapsedSinceReport().toMinutes());

                    priority += ReportCategoryType.valueOf(reason.getMessage()).getPriority() * ageImpact;
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
            sender.sendMessage(new ReportHandleCommand().getNextReport().getReportID());
            return true;
        }

        if (args[0].equalsIgnoreCase("3")) {
            ReportHandleCommand reportHandleCommand = new ReportHandleCommand();
            Report report = reportHandleCommand.getNextReport();
            sender.sendMessage(CC.format("&8&m------------------------------------------------"));
            sender.sendMessage("Report ID: " + report.getReportID());
            sender.sendMessage("Suspect: " + Bukkit.getPlayer(UUID.fromString(report.getSuspectUUID())).getName());
            sender.sendMessage("Priority: " + reportHandleCommand.calculatePriority(report));
            sender.sendMessage("Category: " + report.getCategory());
            sender.sendMessage(report.getReasons().size() + " total reports: ");
            int size = 1;
            for (Reason reason : report.getReasons()) {
                sender.sendMessage("(" + size++ + ") " + Bukkit.getPlayer(UUID.fromString(reason.getUuid())).getName() + ": " + ReportCategoryType.valueOf(reason.getMessage()).getName());
            }
            sender.sendMessage(CC.format("&8&m------------------------------------------------"));
            return true;
        }

        if (args[0].equalsIgnoreCase("4")) {
            for (Report report : ReportService.getPending()) {
                sender.sendMessage(CC.format("&8&m------------------------------------------------"));
                sender.sendMessage("Report ID: " + report.getReportID());
                sender.sendMessage("Suspect: " + Bukkit.getPlayer(UUID.fromString(report.getSuspectUUID())).getName());
                sender.sendMessage("Category: " + report.getCategory());
                sender.sendMessage(report.getReasons().size() + " total reports: ");
                int size = 1;
                for (Reason reason : report.getReasons()) {
                    sender.sendMessage("(" + size++ + ") " + Bukkit.getPlayer(UUID.fromString(reason.getUuid())).getName() + ": " + ReportCategoryType.valueOf(reason.getMessage()).getName());
                }
                if (report.getMessages() != null && !report.getMessages().isEmpty()) {
                    System.out.println("Report ID: " + report.getReportID());
                }
                sender.sendMessage(CC.format("&8&m------------------------------------------------"));
            }
            return true;
        }
        return true;
    }
}
