package games.sparking.crystalguard.reports.menu.close;

import games.sparking.crystalguard.CrystalGuard;
import games.sparking.crystalguard.reports.RejectionTypes;
import games.sparking.crystalguard.reports.Report;
import games.sparking.crystalguard.reports.ReportService;
import games.sparking.crystalguard.utils.CC;
import games.sparking.crystalguard.utils.ItemBuilder;
import games.sparking.crystalguard.utils.menu.Button;
import games.sparking.crystalguard.utils.menu.Menu;
import games.sparking.crystalguard.utils.menu.menu.ConfirmationMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReportRejectMenu extends Menu {

    private Report report;

    public ReportRejectMenu(Report report) {
        this.report = report;
    }

    @Override
    public String getTitle(Player player) {
        return "Reject Report " + player.getName();
    }

    @Override
    public int getSize() {
        return 36;
    }


    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Player target = Bukkit.getPlayer(UUID.fromString(report.getSuspectUUID()));
        buttons.put(4, new HeadButton(target));

        buttons.put(21, new TypeButton(RejectionTypes.INSUFFICIENT_EVIDENCE, report));
        buttons.put(23, new TypeButton(RejectionTypes.ABUSIVE, report));

//        int index = 20;
//        for (RejectionTypes types : RejectionTypes.values()) {
//            buttons.put(index, new TypeButton(types, report));
//            if (++index % 9 == 7) {
//                index += 4;
//            }
//        }
        return buttons;
    }

    @RequiredArgsConstructor
    public class HeadButton extends Button {

        private final Player p;

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.SKULL_ITEM, 3)
                    .setSkullOwner(p.getName())
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Report &d" + p.getName()))
                    .build();
        }
    }

    public class TypeButton extends Button {

        private RejectionTypes rejectionTypes;

        public TypeButton(RejectionTypes rejectionTypes, Report r) {
            this.rejectionTypes = rejectionTypes;
        }

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(rejectionTypes.getMaterial())
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&l" + rejectionTypes.getName()))
                    .setLore(ChatColor.translateAlternateColorCodes('&', "&7&o" + rejectionTypes.getDesc()))
                    .build();
        }

        @Override
        public void click(Player whoClicked, int slot, ClickType clickType, int hotbarButton) {

            new ConfirmationMenu(
                    "Reject Report?",
                    b -> {
                        if (rejectionTypes.isAbusive()) {
                            ReportService.updateStatus(report, "ABUSIVE", whoClicked.getUniqueId().toString(), rejectionTypes.toString());
                            whoClicked.sendMessage(CC.format("&5&l✦ &7Report &d%s&7 has been marked as abusive.", report.getReportID()));
                        } else {
                            ReportService.updateStatus(report, "REJECTED", whoClicked.getUniqueId().toString(), rejectionTypes.toString());
                            whoClicked.sendMessage(CC.format("&5&l✦ &7Report &d%s&7 has been marked as rejected.", report.getReportID()));
                        }
                        CrystalGuard.getReportsInProgress().remove(whoClicked.getPlayer().getUniqueId());
                    }
            ).openMenu(whoClicked);

        }
    }
}