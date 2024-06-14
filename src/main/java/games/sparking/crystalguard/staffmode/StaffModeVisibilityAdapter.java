package games.sparking.crystalguard.staffmode;

import games.sparking.crystalguard.visibility.VisibilityAction;
import games.sparking.crystalguard.visibility.VisibilityAdapter;
import org.bukkit.entity.Player;


public class StaffModeVisibilityAdapter extends VisibilityAdapter {


    public StaffModeVisibilityAdapter() {
        super("Invictus Staff Mode Adapter", 10);
    }

    @Override
    public VisibilityAction canSee(Player player, Player target) {
        StaffMode staffMode = StaffMode.get(player);
        StaffMode targetStaffMode = StaffMode.get(target);
        if (!targetStaffMode.isVanished() && !targetStaffMode.isEnabled())
            return VisibilityAction.NEUTRAL;

        if (targetStaffMode.isEnabled() && !targetStaffMode.isVanished())
            return VisibilityAction.SHOW;

        if (!player.hasPermission("zircon.staff"))
            return VisibilityAction.HIDE;

//        if (staffMode.isEnabled() && ZirconSettings.STAFF_SHOWN.get(player))
//            return VisibilityAction.SHOW;
//
//        if (!ZirconSettings.STAFF_SHOWN.get(player)|| !zircon.getMainConfig().isStaffVisible())
//            return VisibilityAction.HIDE;

        return VisibilityAction.SHOW;
    }
}
