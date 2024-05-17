package games.sparking.spectator.menu.buttons;

import games.sparking.spectator.menu.Button;
import games.sparking.spectator.utils.ItemBuilder;
import games.sparking.spectator.utils.TypeCallable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ConfirmationButton extends Button {

    private boolean bool;
    private TypeCallable<Boolean> callable;
    private String name;

    public ConfirmationButton(boolean bool, TypeCallable<Boolean> callable) {
        this.bool = bool;
        this.callable = callable;
    }

    public ConfirmationButton(boolean bool, String name, TypeCallable<Boolean> callable) {
        this.bool = bool;
        this.callable = callable;
        this.name = name;
    }

    @Override
    public ItemStack getItem(Player player) {
        return new ItemBuilder(Material.WOOL, (short) (bool ? 5 : 14))
                .setDisplayName(bool ? ChatColor.GREEN + "" + ChatColor.BOLD + name : ChatColor.RED + "" + ChatColor.BOLD + name)
                .build();
    }

    @Override
    public void click(Player player, int slot, ClickType clickType, int hotbarButton) {
        player.closeInventory();
        callable.callback(bool);
    }
}
