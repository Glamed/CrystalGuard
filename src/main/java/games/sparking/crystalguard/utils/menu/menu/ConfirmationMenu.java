package games.sparking.crystalguard.utils.menu.menu;

import games.sparking.crystalguard.utils.TypeCallable;
import games.sparking.crystalguard.utils.menu.Button;
import games.sparking.crystalguard.utils.menu.Menu;
import games.sparking.crystalguard.utils.menu.buttons.ConfirmationButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ConfirmationMenu extends Menu {
    @Override
    public boolean isAutoUpdate() {
        return false;
    }

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

    public int getSize() {
        return 45;
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
        return buttons;
    }

//    @Override
//    public FillTemplate getFillTemplate() {
//        return FillTemplate.FILL;
//    }
//
//    @Override
//    public ItemStack getPlaceholderItem(Player player) {
//        return Button.createPlaceholder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).getItem(player);
//    }
}
