package games.sparking.crystalguard.staffmode.commands;

import games.sparking.crystalguard.staffmode.StaffMode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("cw.vanish")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&l✦ &7This command was not recognized&5."));
                return true;
            }
            StaffMode.get(player).toggleEnabled(false);
        }
        return true;
    }
}
