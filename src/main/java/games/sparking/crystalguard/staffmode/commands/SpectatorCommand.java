package games.sparking.crystalguard.staffmode.commands;

import games.sparking.crystalguard.staffmode.menu.SpectatorMenu;
import games.sparking.crystalguard.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectatorCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("cw.staff")) {
                player.sendMessage(CC.format("&5&l✦ &7This command was not recognized&5."));
                return true;
            }

            SpectatorMenu spectatorMenu = new SpectatorMenu();

            if (args.length != 0) {
                Player p = Bukkit.getServer().getPlayer(args[0]);
                if (p != null && player.isOnline()) {
                    spectatorMenu.spectate(player, p);
                    return true;
                }
            }

            new SpectatorMenu().openMenu(player);
        }
        return true;
    }
}
