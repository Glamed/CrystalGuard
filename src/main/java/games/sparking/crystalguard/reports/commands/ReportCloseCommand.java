package games.sparking.crystalguard.reports.commands;

import games.sparking.crystalguard.CrystalGuard;
import games.sparking.crystalguard.reports.Report;
import games.sparking.crystalguard.reports.menu.close.ReportCloseMenu;
import games.sparking.crystalguard.utils.messages.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCloseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command alias, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("cw.staff")) {
            player.sendMessage(CC.format("&5&l✦ &7You don't have permission to use this command."));
            return true;
        }

        Report report = CrystalGuard.getReportsInProgress().get(player.getUniqueId());
        if (report == null) {
            player.sendMessage(CC.format("&5&l✦ &7You are not currently handling a report."));
            return true;
        }

        new ReportCloseMenu(report).openMenu(player);


        return true;
    }
}
