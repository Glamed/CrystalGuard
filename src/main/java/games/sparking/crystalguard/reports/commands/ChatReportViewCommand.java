package games.sparking.crystalguard.reports.commands;

import games.sparking.crystalguard.reports.menu.ChatReportMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatReportViewCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return true;

        new ChatReportMenu(args[0]).openMenu((Player) sender);

        return true;
    }
}
