package games.sparking.crystalguard.utils.menu.buttons;

import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.TypeCallable;
import games.sparking.crystalguard.utils.menu.Button;
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
        Material woolType = bool ? Material.GREEN_WOOL : Material.RED_WOOL;
        ChatColor color = bool ? ChatColor.GREEN : ChatColor.RED;

        return new ItemBuilder(woolType)
                .setDisplayName(color + "" + ChatColor.BOLD + name)
                .build();
    }

    @Override
    public void click(Player player, int slot, ClickType clickType, int hotbarButton) {
        player.closeInventory();
        if (bool) {
            callable.callback(bool);
        }
    }
}
