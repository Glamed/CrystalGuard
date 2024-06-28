package games.sparking.crystalguard.staffmode.listeners;

import games.sparking.crystalguard.staffmode.StaffMode;
import games.sparking.crystalguard.staffmode.menu.ExamineBlockMenu;
import games.sparking.crystalguard.utils.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Openable;
import org.bukkit.material.Redstone;

import java.util.*;


@RequiredArgsConstructor
public class StaffModeListener implements Listener {

    private static final Map<UUID, Location> LAST_LOCATION = new HashMap<>();
    public static String JUMP_TO_TELEPORT_COMMAND = "tp %s";

    private static final List<Material> DENY_INTERACT = Arrays.asList(
            Material.FLINT_AND_STEEL,
            Material.FIREBALL,
            Material.MINECART,
            Material.COMMAND_MINECART,
            Material.EXPLOSIVE_MINECART,
            Material.HOPPER_MINECART,
            Material.STORAGE_MINECART,
            Material.ITEM_FRAME,
            Material.PAINTING,
            Material.SNOW
    );

    private static final List<ItemStack> DROP_PROTECTED = Arrays.asList(
            StaffMode.COMPASS,
            StaffMode.INSPECT,
            StaffMode.AXE,
            StaffMode.RANDOM_TP,
            StaffMode.REPORT,
            StaffMode.VANISH_ON,
            StaffMode.VANISH_OFF
    );

    public static void addDropProtected(ItemStack... items) {
        DROP_PROTECTED.addAll(Arrays.asList(items));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!testBuild(event.getPlayer(), true)) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!testBuild(event.getPlayer(), true)) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        StaffMode staffMode = StaffMode.get(player);
        Block block = event.getClickedBlock();

        if (staffMode.isEnabled() && block != null) {
            if (block.getState() instanceof InventoryHolder && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                InventoryHolder holder = (InventoryHolder) block.getState();
                new ExamineBlockMenu(holder).openMenu(player);
                player.sendMessage(CC.GOLD + "Opening " + CC.WHITE +
                        holder.getInventory().getType().getDefaultTitle() + CC.GOLD + " silently.");
                return;
            }
        }

        if (!staffMode.isEnabled() && !staffMode.isVanished())
            return;


        if (event.getAction() == Action.PHYSICAL) {
            event.setCancelled(true);
            return;
        }

        if (!player.hasPermission("cw.dev")) {
            if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                    && (player.getItemInHand() != null && DENY_INTERACT.contains(player.getItemInHand().getType()))) {
                event.setCancelled(true);
                player.sendMessage(CC.RED + CC.BOLD + "You cannot do this while in staff mode.");
                return;
            }
        }

        if (staffMode.isVanished() || !player.hasPermission("cw.dev")) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                    && block != null && (block.getState().getData() instanceof Openable
                    || block.getState().getData() instanceof Redstone)) {
                event.setCancelled(true);
                player.sendMessage(CC.RED + CC.BOLD + "You cannot do this while in staff mode.");
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        Player player = (Player) event.getEntity().getShooter();
        StaffMode staffMode = StaffMode.get(player);

        if (!staffMode.isEnabled() && !staffMode.isVanished())
            return;

        if (staffMode.isVanished() || !player.hasPermission("cw.staff")) {
            event.setCancelled(true);
            player.sendMessage(CC.RED + CC.BOLD + "You cannot do this while in staff mode.");
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!testBuild(player, true) && event.getRightClicked() instanceof Vehicle) event.setCancelled(true);
    }

    @EventHandler
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        if (!(event.getAttacker() instanceof Player))
            return;

        Player player = (Player) event.getAttacker();
        if (!testBuild(player, true)) event.setCancelled(true);
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        if (!(event.getAttacker() instanceof Player))
            return;

        Player player = (Player) event.getAttacker();
        if (!testBuild(player, true))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        if (!testBuild(player, true)) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        if (!testBuild(player, true)) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        StaffMode staffMode = StaffMode.get(event.getPlayer());
        if (staffMode.isVanished() || staffMode.isEnabled()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        StaffMode staffMode = StaffMode.get(player);

        if (!staffMode.isEnabled() && !staffMode.isVanished())
            return;

        if (!player.hasPermission("cw.dev")) {
            event.setCancelled(true);
            return;
        }

        for (ItemStack itemStack : DROP_PROTECTED) {
            if (event.getItemDrop().getItemStack().isSimilar(itemStack)) {
                event.setCancelled(true);
                return;
            }
        }

        event.getItemDrop().remove();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player player;
        if (event.getDamager() instanceof Player)
            player = (Player) event.getDamager();
        else if (event.getDamager() instanceof Projectile
                && ((Projectile) event.getDamager()).getShooter() instanceof Player)
            player = (Player) ((Projectile) event.getDamager()).getShooter();
        else return;

        StaffMode staffMode = StaffMode.get(player);

        if (staffMode.isEnabled() && (staffMode.isVanished() || !player.hasPermission("cw.staff"))) {
            event.setCancelled(true);
            player.sendMessage(CC.RED + CC.BOLD + "You cannot do this while in staff mode.");
        }

        if (!event.isCancelled()) {
            StaffMode.setLastHit(player.getUniqueId());
            StaffMode.setLastHitTime(System.currentTimeMillis());
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        StaffMode staffMode = StaffMode.get(player);
        if (staffMode.isEnabled() || staffMode.isVanished()) event.setCancelled(true);
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (player.hasPermission("cw.staff"))
            LAST_LOCATION.put(player.getUniqueId(), player.getLocation());

        if (StaffMode.isStaffMode(player))
            event.getDrops().clear();
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause().name().contains("PEARL") || event.getCause().name().contains("PORTAL")
                || !event.getPlayer().hasPermission("cw.staff"))
            return;

        LAST_LOCATION.put(event.getPlayer().getUniqueId(), event.getFrom());
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        if (StaffMode.isStaffMode(player)) {
            event.setCancelled(true);
            event.setFoodLevel(20);
            player.setSaturation(10F);
        }
    }

    private boolean testBuild(Player player, boolean message) {
        StaffMode staffMode = StaffMode.get(player);

        if (!staffMode.isEnabled() && !staffMode.isVanished())
            return true;

        if (staffMode.isEnabled() && player.hasPermission("cw.dev"))
            return true;

        if (message)
            player.sendMessage(CC.RED + CC.BOLD + "You cannot do this while in staff mode.");
        return false;
    }

    public static void removeLastLocation(Player player) {
        LAST_LOCATION.remove(player.getUniqueId());
    }

    public static Location getLastLocation(Player player) {
        return LAST_LOCATION.get(player.getUniqueId());
    }
}
