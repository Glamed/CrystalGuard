package games.sparking.crystalguard.staffmode;

import games.sparking.crystalguard.CrystalGuard;
import games.sparking.crystalguard.staffmode.menu.ExamineMenu;
import games.sparking.crystalguard.staffmode.menu.OnlineStaffMenu;
import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.menu.hotbaritem.HotbarItem;
import games.sparking.crystalguard.utils.messages.CC;
import games.sparking.crystalguard.visibility.VisibilityService;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

@Data
@RequiredArgsConstructor
public class StaffMode {

    public static final ItemStack COMPASS = new ItemBuilder(Material.COMPASS)
            .setDisplayName(ChatColor.DARK_RED + "Teleport Compass")
            .build();

    public static final ItemStack INSPECT = new ItemBuilder(Material.BOOK)
            .setDisplayName(ChatColor.DARK_RED + "Examine Inventory")
            .build();

    public static final ItemStack CARPET = new ItemBuilder(Material.ORANGE_CARPET)
            .setDisplayName(" ")
            .build();

    public static final ItemStack AXE = new ItemBuilder(Material.WOODEN_AXE)
            .setDisplayName(ChatColor.DARK_RED + "World Edit")
            .build();

    public static final ItemStack RANDOM_TP = new ItemBuilder(Material.NETHER_STAR)
            .setDisplayName(ChatColor.DARK_RED + "Random Teleport")
            .build();

    public static final ItemStack REPORT = new ItemBuilder(Material.ANVIL)
            .setDisplayName(ChatColor.DARK_RED + "Handle Report")
            .build();

    public static final ItemStack VANISH_ON = new ItemBuilder(Material.LIME_DYE)
            .setDisplayName(ChatColor.GREEN + "Vanished Enabled - Click to unvanish")
            .build();

    public static final ItemStack VANISH_OFF = new ItemBuilder(Material.GRAY_DYE)
            .setDisplayName(ChatColor.DARK_RED + "Vanished Disabled - Click to vanish")
            .build();

    @Getter
    private static final List<UUID> openInventories = new ArrayList<>();

    private static final HashSet<Material> TRANSPARENT = new HashSet<>();

    private static final Map<UUID, StaffMode> STAFF_MODE_MAP = new HashMap<>();

    public static StaffMode get(Player player) {
        STAFF_MODE_MAP.putIfAbsent(player.getUniqueId(), new StaffMode(player.getUniqueId()));
        return STAFF_MODE_MAP.get(player.getUniqueId());
    }

    public static boolean isVanished(Player player) {
        return get(player).isVanished();
    }

    public static boolean isStaffMode(Player player) {
        return get(player).isEnabled();
    }

    @Getter
    @Setter
    private static UUID lastHit;
    @Getter
    @Setter
    private static Long lastHitTime = System.currentTimeMillis();

    private final UUID uuid;

    private ItemStack[] inventory = new ItemStack[36];
    private ItemStack[] armor = new ItemStack[4];
    private GameMode gameMode = GameMode.SURVIVAL;

    private boolean enabled = false;
    private boolean vanished = false;

    private HotbarItem inspectItem;
    private HotbarItem teleportItem;
    private HotbarItem vanishItem;
    private HotbarItem onlineStaffItem;
    private HotbarItem reportItem;

    private Entity despawningEntity;

    public void toggleEnabled(boolean silent) {

        Player player = Bukkit.getPlayer(uuid);

        this.enabled = !enabled;

        if (!this.enabled) {
            if (this.vanished) {
                this.toggleVanish(true);
            }

            HotbarItem.unregisterItem(player, this.inspectItem.getClass());
            HotbarItem.unregisterItem(player, this.teleportItem.getClass());
            HotbarItem.unregisterItem(player, this.reportItem.getClass());
            HotbarItem.unregisterItem(player, this.vanishItem.getClass());
            HotbarItem.unregisterItem(player, this.onlineStaffItem.getClass());

            player.getInventory().setContents(this.inventory);
            player.getInventory().setArmorContents(this.armor);
            player.setGameMode(this.gameMode);

        } else {

            this.inventory = player.getInventory().getContents();
            this.armor = player.getInventory().getArmorContents();
            this.gameMode = player.getGameMode();

            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setGameMode(GameMode.CREATIVE);

            if (!this.vanished) {
                this.toggleVanish(true);
            }

            this.inspectItem = new InspectItem(player);
            this.teleportItem = new TeleportItem(player);
            this.vanishItem = new VanishItem(player);
            this.onlineStaffItem = new OnlineStaffItem(player);
            this.reportItem = new ReportItem(player);

            int slot = 0;
            player.getInventory().setItem(slot++, this.inspectItem.getItem());

            player.getInventory().setItem(slot, this.reportItem.getItem());
            player.getInventory().setItem(slot = 6, this.teleportItem.getItem());
            player.getInventory().setItem(++slot, this.onlineStaffItem.getItem());
            player.getInventory().setItem(++slot, this.vanishItem.getItem());
        }

        if (!silent)
            player.sendMessage(ChatColor.GOLD + "Staff Mode: " + CC.colorBoolean(enabled, true));

    }

    public void toggleVanish(boolean silent) {

        Player player = Bukkit.getPlayer(uuid);

        this.vanished = !vanished;

        if (this.enabled) {
            if (this.vanishItem == null) {
                this.vanishItem = new VanishItem(player);
            }
            assert player != null;
            player.getInventory().setItem(8, this.vanishItem.getItem());
        }

        if (vanished) {
            VisibilityService.update(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, PotionEffect.INFINITE_DURATION, 0, false, false));
            player.setMetadata("vanished", new FixedMetadataValue(CrystalGuard.getInstance(), "true"));
        } else {
            VisibilityService.update(player);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.removeMetadata("vanished", CrystalGuard.getInstance());
        }

        if (!silent) {
            assert player != null;
            player.sendMessage(ChatColor.GOLD + "Vanish: " + CC.colorBoolean(vanished, true));
        }
    }

    public class InspectItem extends HotbarItem {

        public InspectItem(Player player) {
            super(player);
        }

        @Override
        public ItemStack getItem() {
            return INSPECT;
        }

        @Override
        public void click(Action action, Block block) {
        }

        @Override
        public void clickEntity(Entity entity) {
            if (entity instanceof Player) {
                Player player = Bukkit.getPlayer(uuid);
//                player.openInventory(((Player) entity).getInventory());
                new ExamineMenu((Player) entity).openMenu(player);
            }
        }
    }

    public class TeleportItem extends HotbarItem {

        private final Player player;

        public TeleportItem(Player player) {
            super(player);
            this.player = player;
        }

        @Override
        public ItemStack getItem() {
            return RANDOM_TP;
        }

        @Override
        public void click(Action action, Block block) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                Bukkit.dispatchCommand(player, "rtp");
            }
        }

        @Override
        public void clickEntity(Entity entity) {
        }
    }

    public class ReportItem extends HotbarItem {

        private final Player player;

        public ReportItem(Player player) {
            super(player);
            this.player = player;
        }

        @Override
        public ItemStack getItem() {
            return REPORT;
        }

        @Override
        public void click(Action action, Block block) {
            Bukkit.dispatchCommand(player, "reporthandle");

        }

        @Override
        public void clickEntity(Entity entity) {
        }
    }

    public class VanishItem extends HotbarItem {

        public VanishItem(Player player) {
            super(player);
        }

        @Override
        public ItemStack getItem() {

            return vanished ? VANISH_ON : VANISH_OFF;
        }

        @Override
        public void click(Action action, Block block) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
                toggleVanish(true);
        }

        @Override
        public void clickEntity(Entity entity) {
        }
    }

    public class OnlineStaffItem extends HotbarItem {

        private final Player player;

        public OnlineStaffItem(Player player) {
            super(player);
            this.player = player;
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.PLAYER_HEAD)
                    .setDisplayName(ChatColor.DARK_RED + "Online Staff")
                    .setSkullOwner(player.getName())
                    .build();
        }

        @Override
        public void click(Action action, Block block) {
            Player player = Bukkit.getPlayer(uuid);
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                new OnlineStaffMenu().openMenu(player);
            }
        }

        @Override
        public void clickEntity(Entity entity) {
        }
    }

    static {
        for (Material material : Material.values()) {
            if (material.isTransparent())
                TRANSPARENT.add(material);
        }
    }
}
