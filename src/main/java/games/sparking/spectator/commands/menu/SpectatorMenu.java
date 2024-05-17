package games.sparking.spectator.commands.menu;

import games.sparking.spectator.Spectator;
import games.sparking.spectator.menu.Button;
import games.sparking.spectator.menu.buttons.BackButton;
import games.sparking.spectator.menu.page.PagedMenu;
import games.sparking.spectator.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SpectatorMenu extends PagedMenu {

    @Override
    public String getRawTitle(Player player) {
        return "Spectate " + Bukkit.getOnlinePlayers().size() + " players";
    }


    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            buttons.put(index++, new SpectatorButton(p));
        }
        return buttons;
    }


    @RequiredArgsConstructor
    public class SpectatorButton extends Button {

        private final Player p;

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.SKULL_ITEM, 3)
                    .setSkullOwner(p.getName())
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&l" + p.getName()))
                    .build();
        }

        @Override
        public void click(Player whoClicked, int slot, ClickType clickType, int hotbarButton) {
            spectate(whoClicked, p);
        }
    }


    public void spectate(Player staff, Player target) {
        staff.getOpenInventory().close();
        staff.teleport(target);
        if (!Spectator.getVanished().contains(staff)) {
            staff.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&l✦ &7You are now spectating " + target.getName() + "&5."));
            Spectator.getVanished().add(staff);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.hidePlayer(staff);
            }
            staff.setAllowFlight(true);
            staff.setFlying(true);
        }
    }
}
