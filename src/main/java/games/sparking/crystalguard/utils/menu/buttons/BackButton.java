package games.sparking.crystalguard.utils.menu.buttons;

import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.menu.Button;
import games.sparking.crystalguard.utils.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BackButton extends Button {

    private Menu menu;

    public BackButton(Menu menu) {
        this.menu = menu;
    }

    @Override
    public ItemStack getItem(Player player) {
        /*List<String> lore = new ArrayList<>();
        if (menu instanceof PagedMenu) {
            lore.add(CC.GRAY + "To: " + ((PagedMenu) menu).getRawTitle(player));
        } else {
            lore.add(CC.GRAY + "To: " + menu.getTitle(player));
        }*/
        return new ItemBuilder(Material.BED).setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Go Back").build();
    }

    @Override
    public void click(Player player, int slot, ClickType clickType, int hotbarButton) {
        menu.openMenu(player);
    }
}
