package games.sparking.crystalguard.utils.menu.page;

import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PageButton extends Button {

    private int mod;
    private PagedMenu menu;

    public PageButton(int mod, PagedMenu menu) {
        this.mod = mod;
        this.menu = menu;
    }


    @Override
    public ItemStack getItem(Player player) {
        if (this.hasNext(player)) {
            return new ItemBuilder(Material.LIME_CARPET)
                    .setDisplayName(mod > 0 ? ChatColor.GREEN.toString() + ChatColor.BOLD + "Next Page"
                            : ChatColor.GREEN.toString() + ChatColor.BOLD + "Previous Page")
                    .build();
        } else {
            return new ItemBuilder(Material.GRAY_CARPET)
                    .setDisplayName(mod > 0 ? ChatColor.GRAY.toString() + ChatColor.BOLD + "Next Page"
                            : ChatColor.GRAY.toString() + ChatColor.BOLD + "Previous Page")
                    //.setLore(CC.RED + "You are already on", CC.RED + "the " + (mod > 0 ? "Last " : "First ") + "Page")
                    .build();
        }
    }

    @Override
    public void click(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (clickType.isShiftClick()) {
            if (hasNext(player)) {
                this.menu.modPage(player, this.mod > 0 ?
                        this.menu.getPages(player) - this.menu.getPage() :
                        1 - this.menu.getPage());
            }
        } else {
            if (hasNext(player)) {
                this.menu.modPage(player, mod);
            }
        }
    }

    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0 && this.menu.getPages(player) >= pg;
    }

}
