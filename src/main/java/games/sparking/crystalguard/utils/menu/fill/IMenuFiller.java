package games.sparking.crystalguard.utils.menu.fill;

import games.sparking.crystalguard.utils.menu.Button;
import games.sparking.crystalguard.utils.menu.Menu;
import org.bukkit.entity.Player;

import java.util.Map;

public interface IMenuFiller {

    void fill(Menu menu, Player player, Map<Integer, Button> buttons, int size);

}
