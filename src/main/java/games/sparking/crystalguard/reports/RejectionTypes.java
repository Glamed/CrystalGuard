package games.sparking.crystalguard.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
@Getter
public enum RejectionTypes {

    INSUFFICIENT_EVIDENCE(false, Material.BOOK, "Insufficient Evidence", "Lacking probable cause to issue a punishment"),
    ABUSE(true, Material.REDSTONE_BLOCK, "Abusive", "The reporter(s) were abusing the report system");

    final boolean abusive;
    final Material material;
    final String name;
    final String desc;

}
