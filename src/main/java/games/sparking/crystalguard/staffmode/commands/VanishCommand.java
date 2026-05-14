package games.sparking.crystalguard.staffmode.commands;

import games.sparking.crystalguard.staffmode.StaffMode;
import games.sparking.crystalguard.utils.messages.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("cw.staff")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&l✦ &7This command was not recognized&5."));
                return true;
            }
            if (!StaffMode.get(player).isEnabled()) {
                player.sendMessage(CC.format("&5&l✦ &7You must be in staff mode to use this command&5."));
                return true;
            }

            StaffMode.get(player).toggleVanish(false);
        }
        return true;
    }
}
