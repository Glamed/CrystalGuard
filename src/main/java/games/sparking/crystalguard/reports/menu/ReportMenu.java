package games.sparking.crystalguard.reports.menu;

import games.sparking.crystalguard.menu.Button;
import games.sparking.crystalguard.menu.Menu;
import games.sparking.crystalguard.reports.ReportTypes;
import games.sparking.crystalguard.utils.ItemBuilder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Data
public class ReportMenu extends Menu {

    Player player;

    @Override
    public String getTitle(Player player) {
        return "Report " + player.getName();
    }

    @Override
    public int getSize() {
        return 36;
    }


    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(4, new HeadButton(player));

        int index = 20;
        for (ReportTypes types : ReportTypes.values()) {
            buttons.put(index, new TypeButton(types));
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
            return new ItemBuilder(Material.SKULL_ITEM, 3)
                    .setSkullOwner(p.getName())
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Report &d" + p.getName()))
                    .build();
        }
    }

    public class TypeButton extends Button {

        private ReportTypes reportTypes;
        private Player p = null;

        public TypeButton(ReportTypes reportTypes) {
            this.reportTypes = reportTypes;
        }

        public TypeButton(ReportTypes reportTypes, Player p) {
            this.reportTypes = reportTypes;
            this.p = p;
        }

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(reportTypes.getMaterial())
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&l" + reportTypes.getName()))
                    .setLore(ChatColor.translateAlternateColorCodes('&', "&7&o" + reportTypes.getDesc()))
                    .build();
        }

        @Override
        public void click(Player whoClicked, int slot, ClickType clickType, int hotbarButton) {
            if (p == null) new ReportPlayerMenu().openMenu(whoClicked);
        }
    }

}
