package games.sparking.crystalguard.staffmode.menu;

import games.sparking.crystalguard.CrystalGuard;
import games.sparking.crystalguard.menu.Button;
import games.sparking.crystalguard.menu.page.PagedMenu;
import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.Profile;
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
        return "Spectate " + Bukkit.getOnlinePlayers().size() + (Bukkit.getOnlinePlayers().size() > 1 ? " players" : " player");
    }

    @Override
    public int getSize() {
        return 54;
    }

    @Override
    public int getMaxItemsPerPage() {
        return 36;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 0;

        for (Player p : Profile.getPlayersAlphabetically()) {
            buttons.put(slot++, new SpectatorButton(p));
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
        if (!CrystalGuard.getVanished().contains(staff)) {
            staff.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5&l✦ &7You are now spectating " + target.getName() + "&5."));
            CrystalGuard.getVanished().add(staff);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.hidePlayer(staff);
            }
            staff.setAllowFlight(true);
            staff.setFlying(true);
        }
    }
}
