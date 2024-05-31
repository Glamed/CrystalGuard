package games.sparking.crystalguard.menu.fill;

import games.sparking.crystalguard.menu.Button;
import games.sparking.crystalguard.menu.Menu;
import org.bukkit.entity.Player;

import java.util.Map;

public interface IMenuFiller {

    void fill(Menu menu, Player player, Map<Integer, Button> buttons, int size);

}
