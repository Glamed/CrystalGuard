package games.sparking.crystalguard.reports.commands;

import games.sparking.crystalguard.reports.ReportCategoryType;
import games.sparking.crystalguard.reports.ReportManager;
import games.sparking.crystalguard.reports.menu.ReportMenu;
import games.sparking.crystalguard.reports.menu.ReportPlayerMenu;
import games.sparking.crystalguard.utils.menu.menu.ConfirmationMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return true;
        }

        Player player = (Player) sender;
        ReportCategoryType reportCategoryType = getPunishmentType(alias);

        if (args.length == 0) {
            handleNoArgs(reportCategoryType, player);
        } else {
            handleWithArgs(reportCategoryType, player, args[0]);
        }

        return true;
    }

    private ReportCategoryType getPunishmentType(String alias) {
        if (alias.equalsIgnoreCase("hr") ||
                alias.equalsIgnoreCase("hackerreport") ||
                alias.equalsIgnoreCase("hackereport") ||
                alias.equalsIgnoreCase("crystalguardreport") ||
                alias.equalsIgnoreCase("crystalguard") ||
                alias.equalsIgnoreCase("cgr") ||
                alias.equalsIgnoreCase("cg")) {
            return ReportCategoryType.CHEATING;
        } else if (alias.equalsIgnoreCase("chatreport") ||
                alias.equalsIgnoreCase("cr")) {
            return ReportCategoryType.CHAT_ABUSE;
        } else {
            return null;
        }
    }

    private void handleNoArgs(ReportCategoryType reportCategoryType, Player player) {
        if (reportCategoryType == null) {
            new ReportMenu().openMenu(player);
        } else {
            new ReportPlayerMenu(reportCategoryType).openMenu(player);
        }
    }

    private void handleWithArgs(ReportCategoryType reportCategoryType, Player player, String targetName) {
        Player target = Bukkit.getPlayer(targetName);

        if (reportCategoryType == null) {
            if (target == null) {
                new ReportMenu().openMenu(player);
            } else {
                new ReportMenu(target).openMenu(player);
            }
        } else {
            if (target == null) {
                new ReportPlayerMenu(reportCategoryType).openMenu(player);
            } else {
                new ConfirmationMenu(
                        "Report " + target.getName() + "?",
                        confirmed -> {
                            if (confirmed) {
                                ReportManager.create(player, target, reportCategoryType);
                            }
                        }
                ).openMenu(player);
            }
        }
    }
}
