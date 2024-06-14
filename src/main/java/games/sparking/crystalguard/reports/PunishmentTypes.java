package games.sparking.crystalguard.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
@Getter
public enum PunishmentTypes {

    CHAT_ABUSE(ReportTypes.CHAT, 2, Material.BOOK, "Chat Abuse", "Swearing, discrimination, bullying, etc"),
    CHEATING(ReportTypes.GAMEPLAY, 3, Material.IRON_SWORD, "Cheating", "Well... uh hacking, cross teaming, idk man figure it out"),
    BAD_NAME(ReportTypes.GAMEPLAY, 1, Material.NAME_TAG, "Bad Name", "Monkeyjake, NibbaButtz"),
    BAD_SKIN(ReportTypes.GAMEPLAY, 1, Material.ARMOR_STAND, "Bad Skin", "My guy Adolf, nude etc"),
    EXPLOITS(ReportTypes.GAMEPLAY, 2, Material.ANVIL, "Exploits", "... i'm not even");

    final ReportTypes types;
    final int priority;
    final Material material;
    final String name;
    final String desc;

}
