package games.sparking.crystalguard.reports.menu;

import games.sparking.crystalguard.reports.ReportCategoryType;
import games.sparking.crystalguard.reports.ReportManager;
import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.menu.Button;
import games.sparking.crystalguard.utils.menu.Menu;
import games.sparking.crystalguard.utils.menu.menu.ConfirmationMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportMenu extends Menu {

    Player target;

    @Override
    public String getTitle(Player player) {
        return "Report " + player.getName();
    }

    @Override
    public int getSize() {
        if (target != null) {
            return 36;
        }
        return 27;
    }


    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 11;

        if (target != null) {
            buttons.put(4, new HeadButton(player));
            index = 20;
        }

        for (ReportCategoryType types : ReportCategoryType.values()) {
            buttons.put(index, new TypeButton(types, target));
            if (++index % 9 == 7) {
                index += 4;
            }
        }
        return buttons;
    }

    @RequiredArgsConstructor
    public class HeadButton extends Button {

        private final Player p;

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.PLAYER_HEAD)
                    .setSkullOwner(p.getName())
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Report &d" + p.getName()))
                    .build();
        }
    }

    public class TypeButton extends Button {

        private ReportCategoryType reportCategoryType;
        private Player p;

        public TypeButton(ReportCategoryType reportCategoryType, Player p) {
            this.reportCategoryType = reportCategoryType;
            this.p = p;
        }

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(reportCategoryType.getMaterial())
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&l" + reportCategoryType.getName()))
                    .setLore(ChatColor.translateAlternateColorCodes('&', "&7&o" + reportCategoryType.getDesc()))
                    .build();
        }

        @Override
        public void click(Player whoClicked, int slot, ClickType clickType, int hotbarButton) {
            if (p == null) {
                new ReportPlayerMenu(reportCategoryType).openMenu(whoClicked);
                return;
            }


            new ConfirmationMenu(
                    "Report " + p.getName() + "?",
                    b -> {
                        ReportManager.create(whoClicked, p, reportCategoryType);
                    }
            ).openMenu(whoClicked);

        }
    }

}
