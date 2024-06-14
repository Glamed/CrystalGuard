package games.sparking.crystalguard.punish.menu;

import games.sparking.crystalguard.punish.PunishManager;
import games.sparking.crystalguard.punish.PunishmentType;
import games.sparking.crystalguard.reports.PunishmentTypes;
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
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PunishMenu extends Menu {

    OfflinePlayer target;

    public PunishMenu(OfflinePlayer target) {
        this.target = target;
    }

    @Override
    public String getTitle(Player player) {
        return "Punish " + target.getName();
    }

    @Override
    public int getSize() {
        return 36;
    }


    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(4, new HeadButton(target));

        int index = 20;
        for (PunishmentTypes types : PunishmentTypes.values()) {
            buttons.put(index, new TypeButton(types, (Player) target));
            if (++index % 9 == 7) {
                index += 4;
            }
        }
        return buttons;
    }

    @RequiredArgsConstructor
    public class HeadButton extends Button {

        private final OfflinePlayer p;

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.SKULL_ITEM, 3)
                    .setSkullOwner(p.getName())
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Punish &d" + p.getName()))
                    .build();
        }
    }

    public class TypeButton extends Button {

        private PunishmentTypes punishmentTypes;
        private Player p = null;

        public TypeButton(PunishmentTypes punishmentTypes) {
            this.punishmentTypes = punishmentTypes;
        }

        public TypeButton(PunishmentTypes punishmentTypes, Player p) {
            this.punishmentTypes = punishmentTypes;
            this.p = p;
        }

        @Override
        public ItemStack getItem(Player player) {

            return new ItemBuilder(punishmentTypes.getMaterial())
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&l" + punishmentTypes.getName()))
                    .setLore(ChatColor.translateAlternateColorCodes('&', "&7&o" + punishmentTypes.getDesc()))
                    .build();
        }

        @Override
        public void click(Player whoClicked, int slot, ClickType clickType, int hotbarButton) {
            new ConfirmationMenu(
                    "Punish " + target.getName() + "?",
                    b -> {
                        new PunishManager(p, whoClicked, PunishmentType.MUTE, -1, PunishmentTypes.BAD_NAME, "nigger").issue();
                    }
            ).openMenu(whoClicked);
        }
    }
}