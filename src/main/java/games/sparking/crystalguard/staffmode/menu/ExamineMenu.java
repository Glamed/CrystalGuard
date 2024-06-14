package games.sparking.crystalguard.staffmode.menu;


import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.TimeUtils;
import games.sparking.crystalguard.utils.menu.Button;
import games.sparking.crystalguard.utils.menu.Menu;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ExamineMenu extends Menu {

    private final Player target;

    @Override
    public String getTitle(Player player) {
        return "Inventory: " + target.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        if ((target == null) || (!target.isOnline())) {
            player.closeInventory();
            return new HashMap<>();
        }

        Map<Integer, Button> buttons = new HashMap<>();
        for (int index = 0; index < target.getInventory().getContents().length; index++) {
            buttons.put(index, new ItemButton(target.getInventory().getContents()[index]));
        }
        buttons.put(36, new ItemButton(target.getInventory().getHelmet()));
        buttons.put(37, new ItemButton(target.getInventory().getChestplate()));
        buttons.put(38, new ItemButton(target.getInventory().getLeggings()));
        buttons.put(39, new ItemButton(target.getInventory().getBoots()));

        buttons.put(40, Button.createPlaceholder());
        buttons.put(41, Button.createPlaceholder());

        int offset = player.hasPermission("invictus.command.clear.other") ? 0 : 1;
        buttons.put(42 + offset, new HealthButton());
        buttons.put(43 + offset, new EffectButton());
        if (player.hasPermission("invictus.command.clear.other")) {
            buttons.put(44, new ClearButton());
        }
        return buttons;
    }

    @RequiredArgsConstructor
    public class ItemButton extends Button {

        private final ItemStack item;

        @Override
        public ItemStack getItem(Player player) {
            return item;
        }
    }

    public class HealthButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.INK_SACK, DyeColor.RED.getDyeData())
                    .setDisplayName(ChatColor.GOLD + "Player's Health")
                    .setAmount((int) target.getHealth())
                    .build();
        }
    }

    public class EffectButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            List<String> lore = new ArrayList<>();
            target.getActivePotionEffects().forEach(effect -> {
                String effectName = WordUtils.capitalizeFully(effect.getType().getName().replace("_", " "));
                String effectDuration = TimeUtils.formatHHMMSS(effect.getDuration() / 20);
                lore.add(ChatColor.GOLD + effectName + " " + (effect.getAmplifier() + 1) + ": " + ChatColor.WHITE + effectDuration);
            });

            return new ItemBuilder(Material.BLAZE_POWDER)
                    .setDisplayName(ChatColor.GOLD + "Active Effects")
                    .setLore(lore)
                    .build();
        }
    }

    public class ClearButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.BOOK_AND_QUILL)
                    .setDisplayName(ChatColor.GOLD + "Clear Inventory")
                    .build();
        }

        @Override
        public void click(Player player, int slot, ClickType clickType, int hotbarButton) {
            Bukkit.dispatchCommand(player, "clear " + target.getName());
        }
    }
}
