package games.sparking.crystalguard.staffmode.commands;

import games.sparking.crystalguard.CrystalGuard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("cw.vanish")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&l✦ &7This command was not recognized&5."));
                return true;
            }

            if (CrystalGuard.getVanished().contains(player)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&l✦ &7You are no longer vanished&5."));
                CrystalGuard.getVanished().remove(player);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.showPlayer(player);
                }
                player.setFlying(false);
                player.setAllowFlight(false);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&l✦ &7You are now vanished&5."));
                CrystalGuard.getVanished().add(player);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.hidePlayer(player);
                }
                player.setAllowFlight(true);
                player.setFlying(true);
            }
        }
        return true;
    }
}
