package games.sparking.spectator.menu.menu;

import games.sparking.spectator.menu.Button;
import games.sparking.spectator.menu.Menu;
import games.sparking.spectator.menu.buttons.ConfirmationButton;
import games.sparking.spectator.menu.fill.FillTemplate;
import games.sparking.spectator.utils.TypeCallable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ConfirmationMenu extends Menu {
    private String title;
    private TypeCallable<Boolean> callable;
    private ItemStack info = null;
    private String acceptName = "Confirm";
    private String denyName = "Cancel";

    public ConfirmationMenu(String title, TypeCallable<Boolean> callable) {
        this.title = title;
        this.callable = callable;
    }

    public ConfirmationMenu(String title, ItemStack info, TypeCallable<Boolean> callable) {
        this.title = title;
        this.info = info;
        this.callable = callable;
    }

    public ConfirmationMenu(String title, String acceptName, String denyName, TypeCallable<Boolean> callable) {
        this.title = title;
        this.callable = callable;
        this.acceptName = acceptName;
        this.denyName = denyName;
    }

    public ConfirmationMenu(String title, ItemStack info, String acceptName, String denyName,
                            TypeCallable<Boolean> callable) {
        this.title = title;
        this.info = info;
        this.callable = callable;
        this.acceptName = acceptName;
        this.denyName = denyName;
    }

    @Override
    public String getTitle(Player player) {
        return title;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(10, new ConfirmationButton(true, acceptName, callable));
        buttons.put(11, new ConfirmationButton(true, acceptName, callable));
        buttons.put(12, new ConfirmationButton(true, acceptName, callable));
        buttons.put(19, new ConfirmationButton(true, acceptName, callable));
        buttons.put(20, new ConfirmationButton(true, acceptName, callable));
        buttons.put(21, new ConfirmationButton(true, acceptName, callable));
        buttons.put(28, new ConfirmationButton(true, acceptName, callable));
        buttons.put(29, new ConfirmationButton(true, acceptName, callable));
        buttons.put(30, new ConfirmationButton(true, acceptName, callable));


        buttons.put(14, new ConfirmationButton(false, denyName, callable));
        buttons.put(15, new ConfirmationButton(false, denyName, callable));
        buttons.put(16, new ConfirmationButton(false, denyName, callable));
        buttons.put(23, new ConfirmationButton(false, denyName, callable));
        buttons.put(24, new ConfirmationButton(false, denyName, callable));
        buttons.put(25, new ConfirmationButton(false, denyName, callable));
        buttons.put(32, new ConfirmationButton(false, denyName, callable));
        buttons.put(33, new ConfirmationButton(false, denyName, callable));
        buttons.put(34, new ConfirmationButton(false, denyName, callable));
        buttons.put(36, Button.createPlaceholder(new ItemStack(Material.STAINED_GLASS, 1, (short) 7)));
        return buttons;
    }

    @Override
    public FillTemplate getFillTemplate() {
        return FillTemplate.FILL;
    }

    @Override
    public ItemStack getPlaceholderItem(Player player) {
        return Button.createPlaceholder( new ItemStack(Material.STAINED_GLASS, 1, (short) 7)).getItem(player);
    }
}
