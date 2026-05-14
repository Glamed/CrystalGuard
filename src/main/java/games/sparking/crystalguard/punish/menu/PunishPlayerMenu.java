package games.sparking.crystalguard.punish.menu;

import games.sparking.crystalguard.reports.ReportCategoryType;
import games.sparking.crystalguard.reports.ReportManager;
import games.sparking.crystalguard.reports.menu.ReportMenu;
import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.Profile;
import games.sparking.crystalguard.utils.menu.Button;
import games.sparking.crystalguard.utils.menu.buttons.BackButton;
import games.sparking.crystalguard.utils.menu.menu.ConfirmationMenu;
import games.sparking.crystalguard.utils.menu.page.PagedMenu;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class PunishPlayerMenu extends PagedMenu {

    ReportCategoryType type;

    @Override
    public String getRawTitle(Player player) {
        return "Punish " + Bukkit.getOnlinePlayers().size() + (Bukkit.getOnlinePlayers().size() > 1 ? " players" : " player");
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
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(4, new BackButton(new ReportMenu()));
        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 11;

        for (Player p : Profile.getPlayersAlphabetically()) {
            buttons.put(index++, new ReportHeads(p));
        }
        return buttons;
    }

    @RequiredArgsConstructor
    public class ReportHeads extends Button {

        private final Player p;

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.PLAYER_HEAD)
                    .setSkullOwner(p.getName())
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Punish &d" + p.getName()))
                    .build();
        }

        @Override
        public void click(Player whoClicked, int slot, ClickType clickType, int hotbarButton) {
            if (clickType == ClickType.LEFT || clickType == ClickType.RIGHT) {
                new ConfirmationMenu(
                        "Punish " + p.getName() + "?",
                        b -> {
                            ReportManager.create(whoClicked, p, type);
                        }
                ).openMenu(whoClicked);
            }
        }
    }
}
