package games.sparking.crystalguard.reports;

import org.bukkit.Material;

public enum ReportTypes {

    CHAT_ABUSE(Material.BOOK, "Chat Abuse", "Swearing, discrimination, bullying, etc"),
    CHEATING(Material.IRON_SWORD, "Cheating", "Well... uh hacking, cross teaming, idk man figure it out"),
    BAD_NAME(Material.NAME_TAG, "Bad Name", "Monkeyjake, NibbaButtz"),
    BAD_SKIN(Material.ARMOR_STAND, "Bad Skin", "My guy Adolf, nude etc"),
    EXPLOITS(Material.ANVIL, "Exploits", "... i'm not even");

    Material material;
    String name;
    String desc;

    ReportTypes(Material material, String name, String desc) {
        this.material = material;
        this.name = name;
        this.desc = desc;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
