package games.sparking.crystalguard.punish.commands;

import games.sparking.crystalguard.punish.menu.PunishMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PunishCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length == 0) return true;

        new PunishMenu(Bukkit.getOfflinePlayer(args[0])).openMenu((Player) sender);
        return true;
    }
}
