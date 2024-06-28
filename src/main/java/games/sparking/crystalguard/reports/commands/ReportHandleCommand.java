package games.sparking.crystalguard.reports.commands;

import games.sparking.crystalguard.CrystalGuard;
import games.sparking.crystalguard.reports.PunishmentTypes;
import games.sparking.crystalguard.reports.Reason;
import games.sparking.crystalguard.reports.Report;
import games.sparking.crystalguard.reports.ReportService;
import games.sparking.crystalguard.staffmode.menu.SpectatorMenu;
import games.sparking.crystalguard.utils.CC;
import games.sparking.crystalguard.utils.ChatMessage;
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

        try {
            for (Report report : ReportService.getUnhandled()) {
                //Bukkit.broadcastMessage(ChatColor.YELLOW + "Processing report: " + report.getReportID());

                double priority = calculatePriority(report);
                //Bukkit.broadcastMessage(ChatColor.YELLOW + "Calculated priority: " + priority);

                if (report.getHandler() != null) {
                    UUID handlerUUID = null;
                    try {
                        handlerUUID = UUID.fromString(report.getHandler());
                    } catch (IllegalArgumentException e) {
                        //Bukkit.broadcastMessage(ChatColor.RED + "Invalid handler UUID: " + report.getHandler());
                    }
                    Player handler = handlerUUID != null ? Bukkit.getPlayer(handlerUUID) : null;

                    if (handler == null || !handler.isOnline()) {
                        //Bukkit.broadcastMessage(ChatColor.YELLOW + "Handler is offline or null for report: " + report.getReportID());

                        if (report.getStatusTime() != null && System.currentTimeMillis() - report.getStatusTime() > 0.5 * 60 * 1000) {
                            //Bukkit.broadcastMessage(ChatColor.YELLOW + "Report has been in status for more than 30 seconds: " + report.getReportID());

                            if (priority < 1) {
                                expireReport(report);
                                //Bukkit.broadcastMessage(ChatColor.YELLOW + "Expired report: " + report.getReportID());
                                continue; // Skip this report as it's handled here
                            } else {
                                ReportService.updateStatus(report, "PENDING", null);
                                //Bukkit.broadcastMessage(ChatColor.YELLOW + "Updated status to PENDING for report: " + report.getReportID());
                                // Continue to add the report to the TreeMap
                            }
                        }
                    }
                }

                if (report.getHandler() == null && priority < 1) {
                    expireReport(report);
                    //Bukkit.broadcastMessage(ChatColor.YELLOW + "Expired report (no handler and low priority): " + report.getReportID());
                    continue; // Skip this report as it's expired
                }

                if (report.getHandler() == null) {
                    reports.put(priority, report);
                    //Bukkit.broadcastMessage(ChatColor.YELLOW + "Added report to TreeMap with priority: " + priority + " for report: " + report.getReportID());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Bukkit.broadcastMessage(ChatColor.RED + "An error occurred while getting the next report. Please check the console for details.");
        }

        Report nextReport = reports.isEmpty() ? null : reports.firstEntry().getValue();
        if (nextReport != null) {
            //Bukkit.broadcastMessage(ChatColor.GREEN + "Next report to handle: " + nextReport.getReportID());
        } else {
            //Bukkit.broadcastMessage(ChatColor.GREEN + "No reports to handle.");
        }

        return nextReport;
    }


    private double calculatePriority(Report report) {
        double priority = 30;
        //Bukkit.broadcastMessage(report.getReasons().size() + "");
        for (Reason reason : report.getReasons()) {
            //Bukkit.broadcastMessage("69");
            double ageImpact = Math.pow(0.95, reason._getTimeElapsedSinceReport().toMinutes());
            priority += 5 * ageImpact;
            priority += PunishmentTypes.valueOf(reason.getMessage()).getPriority() * ageImpact;
        }

        return priority;
    }

    private void expireReport(Report report) {
        try {
            ReportService.updateStatus(report, "EXPIRED", null);
            //Bukkit.broadcastMessage(CC.format("&5&l✦ &7Report &f" + report.getReportID() + "&7 expired."));
        } catch (Exception e) {
            e.printStackTrace();
            //Bukkit.broadcastMessage(ChatColor.RED + "An error occurred while expiring a report. Please check the console for details.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("cw.staff")) {
            player.sendMessage(CC.format("&5&l✦ &7You don't have permission to use this command."));
            return true;
        }

        try {
            Report main = CrystalGuard.getReportsInProgress().get(player.getUniqueId());
            if (main != null) {
                displayReport(player, main);
                return true; // Already handling a report, so return here
            }

            Report containing = ReportService.getByHandler(player.getUniqueId().toString());
            if (containing != null) {
                displayReport(player, containing);
                CrystalGuard.getReportsInProgress().put(player.getUniqueId(), containing);
                return true;
            }

            Report report = getNextReport();

            if (report == null) {
                player.sendMessage(CC.format("&5&l✦ &7There are no open reports. Good job!"));
                return true;
            }

            CrystalGuard.getReportsInProgress().put(player.getUniqueId(), report);
            ReportService.updateStatus(report, "IN-PROGRESS", player.getUniqueId().toString());

            displayReport(player, report);

            Player target = Bukkit.getPlayer(UUID.fromString(report.getSuspectUUID()));

            new SpectatorMenu().spectate(player, target);
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "An error occurred while executing the command. Please notify a developer ASAP.");
        }

        return true;
    }

    public void displayReport(Player player, Report report) {
        player.sendMessage(CC.format("&8&m------------------------------------------------"));
        player.sendMessage(CC.format(" &7Report ID&8: &d" + report.getReportID()));
        player.sendMessage(CC.format(" &7Suspect&8: &d" + Bukkit.getOfflinePlayer(UUID.fromString(report.getSuspectUUID())).getName()));
        player.sendMessage(CC.format(" &7Category&8: &d" + report.getCategory()));
        player.sendMessage(CC.format(" &7" + report.getReasons().size() + " total report" + (report.getReasons().size() > 1 ? "s" : "") + "&8: "));
        int size = 1;
        for (Reason reason : report.getReasons()) {
            player.sendMessage(CC.format("  &8-&7 " + size++ + "&8: &5" + Bukkit.getOfflinePlayer(UUID.fromString(reason.getUuid())).getName() + ": &d" + PunishmentTypes.valueOf(reason.getMessage()).getName()));
        }

//        ChatMessage message = new ChatMessage(" ");
//        message.add("[").color(ChatColor.DARK_GRAY);
//        message.add("➡").color(ChatColor.RED).hoverText("&7Click to close report");
//        message.add("]").color(ChatColor.DARK_GRAY);
//        if (!report.getMessages().isEmpty()) {
//            message.add(" [").color(ChatColor.DARK_GRAY);
//            message.add("i").color(ChatColor.BLUE).color(ChatColor.BOLD).hoverText("&7Click to view messages").runCommand("/viewchatreport " + report.getReportID());
//            message.add("]").color(ChatColor.DARK_GRAY);
//        }
//        message.send(player);

        Player target = Bukkit.getPlayer(UUID.fromString(report.getSuspectUUID()));

        player.sendMessage("");
        ChatMessage message = new ChatMessage(" - ").color(ChatColor.DARK_GRAY);


        ChatMessage closeReport = new ChatMessage("")
                .runCommand("/reportclose")
                .hoverText(CC.GRAY + "Click here to close this report");
        closeReport.add("[").color(ChatColor.DARK_GRAY);
        closeReport.add("✖").color(ChatColor.RED);
        closeReport.add("]").color(ChatColor.DARK_GRAY);

        message.add(closeReport);
        message.add("");

        ChatMessage spectateReport = new ChatMessage("")
                .runCommand("/spectate " + target.getName())
                .hoverText(CC.GRAY + "Click here to spectate " + target.getName());
        spectateReport.add("[").color(ChatColor.DARK_GRAY);
        spectateReport.add("➡").color(ChatColor.LIGHT_PURPLE);
        spectateReport.add("]").color(ChatColor.DARK_GRAY);

        message.add(spectateReport);
        message.add("");

        ChatMessage viewChatReport = new ChatMessage("")
                .runCommand("/viewchatreport " + report.getReportID())
                .hoverText(CC.GRAY + "Click here to view chat history");
        viewChatReport.add("[").color(ChatColor.DARK_GRAY);
        viewChatReport.add("i").color(ChatColor.AQUA).format(ChatColor.BOLD);
        viewChatReport.add("]").color(ChatColor.DARK_GRAY);

        message.add(viewChatReport);
        message.add("");


        message.send(player);
        player.sendMessage(CC.format("&8&m------------------------------------------------"));
    }
}
