package games.sparking.crystalguard.reports.commands;

import games.sparking.crystalguard.reports.PunishmentTypes;
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
        PunishmentTypes punishmentTypes = getPunishmentType(alias);

        if (args.length == 0) {
            handleNoArgs(punishmentTypes, player);
        } else {
            handleWithArgs(punishmentTypes, player, args[0]);
        }

        return true;
    }

    private PunishmentTypes getPunishmentType(String alias) {
        if (alias.equalsIgnoreCase("hr") ||
                alias.equalsIgnoreCase("hackerreport") ||
                alias.equalsIgnoreCase("hackereport") ||
                alias.equalsIgnoreCase("crystalguardreport") ||
                alias.equalsIgnoreCase("crystalguard") ||
                alias.equalsIgnoreCase("cgr") ||
                alias.equalsIgnoreCase("cg")) {
            return PunishmentTypes.CHEATING;
        } else if (alias.equalsIgnoreCase("chatreport") ||
                alias.equalsIgnoreCase("cr")) {
            return PunishmentTypes.CHAT_ABUSE;
        } else {
            return null;
        }
    }

    private void handleNoArgs(PunishmentTypes punishmentTypes, Player player) {
        if (punishmentTypes == null) {
            new ReportMenu().openMenu(player);
        } else {
            new ReportPlayerMenu(punishmentTypes).openMenu(player);
        }
    }

    private void handleWithArgs(PunishmentTypes punishmentTypes, Player player, String targetName) {
        Player target = Bukkit.getPlayer(targetName);

        if (punishmentTypes == null) {
            if (target == null) {
                new ReportMenu().openMenu(player);
            } else {
                new ReportMenu(target).openMenu(player);
            }
        } else {
            if (target == null) {
                new ReportPlayerMenu(punishmentTypes).openMenu(player);
            } else {
                new ConfirmationMenu(
                        "Report " + target.getName() + "?",
                        confirmed -> {
                            if (confirmed) {
                                ReportManager.create(player, target, punishmentTypes);
                            }
                        }
                ).openMenu(player);
            }
        }
    }
}
