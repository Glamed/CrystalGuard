package games.sparking.crystalguard.reports.commands;

import games.sparking.crystalguard.reports.Report;
import games.sparking.crystalguard.reports.ReportService;
import games.sparking.crystalguard.reports.menu.ChatReportMenu;
import games.sparking.crystalguard.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatReportViewCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        Report report = ReportService.getByID(args[0]);

        if (!player.hasPermission("cw.staff")) {
            player.sendMessage(CC.format("&5&l✦ &7You don't have permission to use this command."));
            return true;
        }

        if (report == null) {
            player.sendMessage(CC.format("&5&l✦ &7Report &d%s&f does not exist.", args[0]));
            return true;
        }


        if (report.getMessages().isEmpty()) {
            player.sendMessage(CC.format("&5&l✦ &7Report &d%s&f does not have a chat history.", args[0]));
            return true;
        }

        new ChatReportMenu(args[0]).openMenu(player);

        return true;
    }
}
