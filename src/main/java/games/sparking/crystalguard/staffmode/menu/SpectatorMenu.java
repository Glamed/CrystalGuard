package games.sparking.crystalguard.staffmode.menu;

import games.sparking.crystalguard.staffmode.StaffMode;
import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.Profile;
import games.sparking.crystalguard.utils.menu.Button;
import games.sparking.crystalguard.utils.menu.menu.ConfirmationMenu;
import games.sparking.crystalguard.utils.menu.page.PagedMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft click to teleport (vanished)"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight click to teleport (unvanished)"));

            return new ItemBuilder(Material.PLAYER_HEAD)
                    .setSkullOwner(p.getName())
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&l" + p.getName()))
                    .setLore(lore)
                    .build();
        }

        @Override
        public void click(Player whoClicked, int slot, ClickType clickType, int hotbarButton) {
            if (clickType == ClickType.LEFT) {
                spectate(whoClicked, p);
            } else if (clickType == ClickType.RIGHT) {
                new ConfirmationMenu(
                        "Teleport to " + p.getName() + " unvanished?",
                        b -> {
                            whoClicked.teleport(p);
                        }
                ).openMenu(whoClicked);

            }
        }
    }


    public void spectate(Player staff, Player target) {
        if (target == null) {
            return;
        }

        staff.getOpenInventory().close();

        if (!StaffMode.isStaffMode(staff)) {
            StaffMode.get(staff).toggleEnabled(false);
        }
        if (!StaffMode.isVanished(staff)) {
            StaffMode.get(staff).toggleVanish(false);
        }

        staff.teleport(target);
    }
}
