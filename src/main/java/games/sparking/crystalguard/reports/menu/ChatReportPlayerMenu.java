package games.sparking.crystalguard.reports.menu;

import games.sparking.crystalguard.reports.MessageCache;
import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.menu.Button;
import games.sparking.crystalguard.utils.menu.buttons.BackButton;
import games.sparking.crystalguard.utils.menu.page.PagedMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ChatReportPlayerMenu extends PagedMenu {

    String reportID;
    MessageCache messageCache;

    public ChatReportPlayerMenu(String reportID, MessageCache messageCache) {
        this.reportID = reportID;
        this.messageCache = messageCache;
    }

    @Override
    public String getRawTitle(Player player) {
        return messageCache.getRecipients().size() + " recipient for message";
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
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(4, new BackButton(new ChatReportMenu(reportID)));
        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 11;

        List<String> reportedByPlayers = new ArrayList<>(messageCache.getReportedBy());
        List<String> recipients = messageCache.getRecipients();

        for (String mc : reportedByPlayers) {
            if (recipients.contains(mc)) {
                buttons.put(index++, new RecipientHeads(ChatColor.LIGHT_PURPLE, Bukkit.getOfflinePlayer(UUID.fromString(mc))));
            }
        }

        // Add other recipients not in reportedByPlayers (if needed)
        for (String mc : recipients) {
            if (!reportedByPlayers.contains(mc)) {
                buttons.put(index++, new RecipientHeads(ChatColor.DARK_PURPLE, Bukkit.getOfflinePlayer(UUID.fromString(mc))));
            }
        }

        return buttons;
    }

    @RequiredArgsConstructor
    public class RecipientHeads extends Button {

        private final ChatColor color;
        private final OfflinePlayer offlinePlayer;

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.PLAYER_HEAD)
                    .setSkullOwner(offlinePlayer.getName())
                    .setDisplayName(color + offlinePlayer.getName())
                    .build();
        }
    }
}
