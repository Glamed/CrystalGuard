package games.sparking.crystalguard.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setDisplayName(String name) {
        this.itemMeta.setDisplayName(name);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        this.itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.itemMeta.setLore(lore);
        return this;
    }

    public ItemBuilder addToLore(String... entries) {
        List<String> lore = itemMeta.hasLore() ? new ArrayList<>(itemMeta.getLore()) : new ArrayList<>();
        lore.addAll(Arrays.asList(entries));
        itemMeta.setLore(lore);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder storeEnchantment(Enchantment enchantment, int level) {
        if (this.itemMeta instanceof EnchantmentStorageMeta)
            ((EnchantmentStorageMeta) this.itemMeta).addStoredEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder setGlowing(boolean glowing) {
        itemMeta.setEnchantmentGlintOverride(glowing);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        if (this.itemMeta instanceof SkullMeta)
            ((SkullMeta) this.itemMeta).setOwner(owner);
        return this;
    }

    public ItemBuilder setArmorColor(Color color) {
        if (this.itemMeta instanceof LeatherArmorMeta)
            ((LeatherArmorMeta) this.itemMeta).setColor(color);
        return this;
    }

    public ItemBuilder setData(short data) {
        this.itemStack.setDurability(data);
        return this;
    }

    public ItemBuilder setData(int data) {
        this.itemStack.setDurability((short) data);
        return this;
    }

    public ItemBuilder setBookAuthor(String author) {
        if (this.itemMeta instanceof BookMeta)
            ((BookMeta) this.itemMeta).setAuthor(author);
        return this;
    }

    public ItemBuilder setBookTitle(String title) {
        if (this.itemMeta instanceof BookMeta)
            ((BookMeta) this.itemMeta).setTitle(title);
        return this;
    }

    public ItemBuilder setBookPages(String... pages) {
        if (this.itemMeta instanceof BookMeta)
            ((BookMeta) this.itemMeta).setPages(pages);
        return this;
    }

    public ItemBuilder setBookPages(List<String> pages) {
        if (this.itemMeta instanceof BookMeta)
            ((BookMeta) this.itemMeta).setPages(pages);
        return this;
    }

    public ItemBuilder addFlag(ItemFlag... flags) {
        this.itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return itemStack;
    }

    @Override
    public ItemBuilder clone() {
        return new ItemBuilder(this.itemStack.clone());
    }
}
