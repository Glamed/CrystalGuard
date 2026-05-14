package games.sparking.crystalguard.punish.menu;

import games.sparking.crystalguard.utils.menu.Button;
import games.sparking.crystalguard.utils.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PunishActionMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Map.of();
    }

    private static class ActionButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            return null;
        }
    }
}
