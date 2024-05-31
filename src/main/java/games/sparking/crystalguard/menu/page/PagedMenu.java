package games.sparking.crystalguard.menu.page;

import games.sparking.crystalguard.menu.Button;
import games.sparking.crystalguard.menu.Menu;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class PagedMenu extends Menu {

    @Getter
    private int page = 1;

    public abstract Map<Integer, Button> getAllPagesButtons(Player player);

    public abstract String getRawTitle(Player player);

    public final int getPages(Player player) {
        int buttonAmount = getAllPagesButtons(player).size();

        if (buttonAmount == 0) {
            return 1;
        }

        return (int) Math.ceil(buttonAmount / (double) getMaxItemsPerPage());
    }

    public final void modPage(Player player, int mod) {
        page += mod;
        getButtons(player).clear();
        openMenu(player);
    }

    @Override
    public final Map<Integer, Button> getButtons(Player player) {
        int minIndex = (page - 1) * getMaxItemsPerPage();
        int maxIndex = page * getMaxItemsPerPage();

        HashMap<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new PageButton(-1, this));
        buttons.put(8, new PageButton(1, this));

        int buttonIndex = 10; // Start from slot 10 for page buttons
        int itemIndex = 0; // Start from index 0 for items

        boolean hasMorePages = false;

        for (Map.Entry<Integer, Button> entry : getAllPagesButtons(player).entrySet()) {
            int ind = entry.getKey();

            if (ind >= minIndex && ind < maxIndex) {
                if (buttonIndex >= getMaxItemsPerPage() + 10) {
                    // Create a new page after reaching the max items per page
                    hasMorePages = true;
                    break; // No need to process further
                }

                buttons.put(buttonIndex, entry.getValue());
                buttonIndex++; // Increment buttonIndex by 1 for each button added

                if (++itemIndex % 7 == 0) {
                    // Every 7 items, increment buttonIndex by 2
                    buttonIndex += 2;
                }
            }
        }

        // Add a new page button only if there are more items beyond the current page
        if (hasMorePages && buttonIndex < getMaxItemsPerPage() + 10) {
            buttons.put(buttonIndex, new PageButton(page + 1, this));
        }

        Map<Integer, Button> global = getGlobalButtons(player);

        if (global != null) {
            for (Map.Entry<Integer, Button> gent : global.entrySet()) {
                buttons.put(gent.getKey(), gent.getValue());
            }
        }

        return buttons;
    }


    public int getMaxItemsPerPage() {
        return 45;
    }

    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    @Override
    public String getTitle(Player player) {
        return "(" + page + "/" + getPages(player) + ") " + getRawTitle(player);
    }
}
