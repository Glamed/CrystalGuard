package games.sparking.crystalguard.punish.menu;

import games.sparking.crystalguard.punish.InfractionType;
import games.sparking.crystalguard.punish.PunishManager;
import games.sparking.crystalguard.punish.PunishmentType;
import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.menu.Button;
import games.sparking.crystalguard.utils.menu.Menu;
import games.sparking.crystalguard.utils.menu.menu.ConfirmationMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PunishMenu extends Menu {

    private final OfflinePlayer target;

    public PunishMenu(OfflinePlayer target) {
        this.target = target;
    }

    @Override
    public String getTitle(Player player) {
        return "Punish " + target.getName();
    }

    @Override
    public int getSize() {
        return 54;
    }


    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(4, new HeadButton(target));


//        int index = 20;
//        for (games.sparking.crystalguard.punish.InfractionType types : games.sparking.crystalguard.punish.InfractionType.values()) {
//            buttons.put(index, new TypeButton(types, (Player) target));
//            if (++index % 9 == 7) {
//                index += 4;
//            }
//        }
//        int index = 19;
//        for (InfractionType types : InfractionType.values()) {
//            buttons.put(index, new TypeButton(types, (Player) target));
//            if (++index % 9 == 8) {
//                index += 2;
//            }
//        }
        int total = InfractionType.visibleValues().length;
        int index = 19;

        for (int i = 0; i < total; ) {
            int itemsThisRow = Math.min(7, total - i);
            int rowStart = (index / 7) * 9;
            int startOffset = (9 - itemsThisRow) / 2;

            for (int j = 0; j < itemsThisRow; j++, i++) {
                int slot = rowStart + startOffset + j;
                buttons.put(slot, new TypeButton(InfractionType.visibleValues()[i], (Player) target));
            }

            index += 7;
        }
        return buttons;
    }

    @RequiredArgsConstructor
    public class HeadButton extends Button {

        private final OfflinePlayer p;

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.PLAYER_HEAD)
                    .setSkullOwner(p.getName())
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Punish &d" + p.getName()))
                    .build();
        }
    }

    public class TypeButton extends Button {

        private final InfractionType infractionType;
        private Player p = null;

        public TypeButton(InfractionType InfractionType) {
            this.infractionType = InfractionType;
        }

        public TypeButton(InfractionType InfractionType, Player p) {
            this.infractionType = InfractionType;
            this.p = p;
        }

        @Override
        public ItemStack getItem(Player player) {
            String desc = infractionType.getDescription();
            List<String> lore = new ArrayList<>();
            StringBuilder line = new StringBuilder();
            for (String w : desc.split(" ")) {
                if (line.length() + w.length() + 1 > 40) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&7&o" + line));
                    line = new StringBuilder(w);
                } else {
                    if (!line.isEmpty()) line.append(" ");
                    line.append(w);
                }
            }
            if (!line.isEmpty())
                lore.add(ChatColor.translateAlternateColorCodes('&', "&7&o" + line));

            return new ItemBuilder(infractionType.getMaterial())
                    .addFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&l" + infractionType.getDisplayName()))
                    .setLore(lore)
                    .build();
        }


        @Override
        public void click(Player whoClicked, int slot, ClickType clickType, int hotbarButton) {
            new ConfirmationMenu(
                    "Punish " + target.getName() + "?",
                    confirmed -> {
                        if (confirmed) {
                            new PunishManager(whoClicked, p, PunishmentType.CHAT_RESTRICTION, -1, infractionType, null).issue();
                        }
                    }
            ).openMenu(whoClicked);
        }
    }
}