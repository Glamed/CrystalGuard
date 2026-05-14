package games.sparking.crystalguard.staffmode.menu;

import games.sparking.crystalguard.staffmode.StaffMode;
import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.Rank;
import games.sparking.crystalguard.utils.menu.Button;
import games.sparking.crystalguard.utils.menu.Menu;
import games.sparking.crystalguard.utils.messages.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@RequiredArgsConstructor
public class OnlineStaffMenu extends Menu {

    public static Map<Player, Rank> sortByPermission(Collection<? extends Player> players) {
        Map<Player, Rank> sortedPlayers = new HashMap<>();

        for (Player player : players) {
            if (player.hasPermission("cw.owner")) {
                sortedPlayers.put(player, Rank.OWNER);
            } else if (player.hasPermission("cw.staff")) {
                sortedPlayers.put(player, Rank.STAFF);
            } else if (player.hasPermission("cw.dev")) {
                sortedPlayers.put(player, Rank.DEV);
            }
        }

        return sortedPlayers;
    }

    @Override
    public String getTitle(Player player) {
        return "Online Staff";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Map<Player, Rank> sortedPlayers = sortByPermission(Bukkit.getOnlinePlayers());

        int index = 0;
        for (Map.Entry<Player, Rank> entry : sortedPlayers.entrySet()) {
            buttons.put(index++, new StaffButton(entry.getKey(), entry.getValue()));
        }
        return buttons;
    }

    @RequiredArgsConstructor
    public class StaffButton extends Button {

        private final Player profile;
        private final Rank rank;

        @Override
        public ItemStack getItem(Player player) {

            StaffMode staffMode = StaffMode.get(player);

            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.LIGHT_PURPLE + "Staff Mode: " + CC.colorBoolean(staffMode.isEnabled(), true));
            lore.add(ChatColor.LIGHT_PURPLE + "Vanished: " + CC.colorBoolean(staffMode.isVanished(), "Yes", "No"));
            lore.add(" ");
            lore.add(ChatColor.YELLOW + "Click to teleport");
            return new ItemBuilder(Material.PLAYER_HEAD)
                    .setSkullOwner(profile.getName())
                    .setDisplayName(CC.format(rank.getPrefix() + "&7 " + profile.getName()))
                    .setLore(lore).build();
        }

        @Override
        public void click(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.teleport(profile);
        }
    }

    @Override
    public boolean isAutoUpdate() {
        return false;
    }

    @Override
    public boolean isClickUpdate() {
        return true;
    }
}
