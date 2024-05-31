package games.sparking.crystalguard.menu.fill.impl;

import games.sparking.crystalguard.menu.Button;
import games.sparking.crystalguard.menu.Menu;
import games.sparking.crystalguard.menu.fill.IMenuFiller;
import games.sparking.crystalguard.menu.page.PagedMenu;
import org.bukkit.entity.Player;

import java.util.Map;

public class FillFiller implements IMenuFiller {

    @Override
    public void fill(Menu menu, Player player, Map<Integer, Button> buttons, int size) {
        for (int i = menu instanceof PagedMenu ? 8 : 0; i < size; i++)
            buttons.putIfAbsent(i, Button.createPlaceholder(menu.getPlaceholderItem(player)));
    }

}
