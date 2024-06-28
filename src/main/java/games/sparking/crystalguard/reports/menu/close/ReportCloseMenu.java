package games.sparking.crystalguard.reports.menu.close;

import games.sparking.crystalguard.reports.Report;
import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.menu.Button;
import games.sparking.crystalguard.utils.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ReportCloseMenu extends Menu {

    private Report report;

    public ReportCloseMenu(Report report) {
        this.report = report;
    }

    @Override
    public String getTitle(Player player) {
        return "Report Close";
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(12, new ReportButton(true));
        buttons.put(14, new ReportButton(false));

        return buttons;
    }


    private class ReportButton extends Button {

        private boolean color;

        public ReportButton(boolean color) {
            this.color = color;
        }

        @Override
        public ItemStack getItem(Player player) {
            if (color) {
                return new ItemBuilder(Material.INK_SACK, DyeColor.GREEN.getDyeData())
                        .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lAccept Report"))
                        .setLore(ChatColor.translateAlternateColorCodes('&', "&7&oYou have probable cause to believe a rule has been violated."))
                        .build();
            } else {
                return new ItemBuilder(Material.INK_SACK, DyeColor.RED.getDyeData())
                        .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lReject Report"))
                        .setLore(ChatColor.translateAlternateColorCodes('&', "&7&oYou do not have probable cause to believe a rule has been violated."))
                        .build();
            }
        }


        @Override
        public void click(Player whoClicked, int slot, ClickType clickType, int hotbarButton) {
            if (color) {
                new ReportAcceptMenu(report).openMenu(whoClicked);
            } else {
                new ReportRejectMenu(report).openMenu(whoClicked);
            }
        }
    }
}
