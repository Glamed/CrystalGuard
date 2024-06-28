package games.sparking.crystalguard.reports.menu;

import games.sparking.crystalguard.punish.menu.PunishMenu;
import games.sparking.crystalguard.reports.MessageCache;
import games.sparking.crystalguard.reports.ReportService;
import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.TimeUtils;
import games.sparking.crystalguard.utils.menu.Button;
import games.sparking.crystalguard.utils.menu.page.PagedMenu;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class ChatReportMenu extends PagedMenu {

    private String id;

    @Override
    public String getRawTitle(Player player) {
        Set<String> players = new HashSet<>();
        for (MessageCache cache : Objects.requireNonNull(ReportService.getByID(id)).getMessages()) {
            players.addAll(cache.getRecipients());
        }
        return "Chat history for " + players.size() + " " + (players.size() > 1 ? " players" : " player");
    }

    @Override
    public int getSize() {
        return 54;
    }

    @Override
    public int getMaxItemsPerPage() {
        return 36;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 11;

        List<MessageCache> messages = Objects.requireNonNull(ReportService.getByID(id)).getMessages();
        for (int i = messages.size() - 1; i >= 0; i--) {
            MessageCache mc = messages.get(i);
            buttons.put(index++, new ChatHeads(mc));
        }
        return buttons;
    }

    @RequiredArgsConstructor
    public class ChatHeads extends Button {

        private final MessageCache mc;

        @Override
        public ItemStack getItem(Player player) {

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(mc.getUuid()));

            List<String> lore = new ArrayList<>();
//            lore.add("");
            lore.add(ChatColor.translateAlternateColorCodes('&', "&7Sent on&8:&d " + TimeUtils.formatDate(mc.getTime())));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&7Message&8: "));
            lore.add(ChatColor.translateAlternateColorCodes('&', " &8-&d " + mc.getMessage()));
            lore.add("");
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight click to view message receivers"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&eLeft click to punish"));


            return new ItemBuilder(Material.SKULL_ITEM, 3)
                    .setSkullOwner(offlinePlayer.getName())
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5" + offlinePlayer.getName()))
                    .setLore(lore)
                    .build();
        }

        @Override
        public void click(Player whoClicked, int slot, ClickType clickType, int hotbarButton) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(mc.getUuid()));

            if (clickType == ClickType.RIGHT) {
                new ChatReportPlayerMenu(id, mc).openMenu(whoClicked);
            } else if (clickType == ClickType.LEFT) {
                new PunishMenu(offlinePlayer).openMenu(whoClicked);
            }
        }
    }
}
