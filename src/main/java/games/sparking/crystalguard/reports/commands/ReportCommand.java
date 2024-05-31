package games.sparking.crystalguard.reports.commands;

import games.sparking.crystalguard.reports.menu.ReportMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("no fuck off jake");
            return true;
        }

        new ReportMenu().openMenu((Player)sender);

        return true;
    }
}
