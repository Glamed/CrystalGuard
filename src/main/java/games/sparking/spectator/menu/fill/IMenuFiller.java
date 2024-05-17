package games.sparking.spectator.menu.fill;

import games.sparking.spectator.menu.Button;
import games.sparking.spectator.menu.Menu;
import org.bukkit.entity.Player;

import java.util.Map;

public interface IMenuFiller {

    void fill(Menu menu, Player player, Map<Integer, Button> buttons, int size);

}
