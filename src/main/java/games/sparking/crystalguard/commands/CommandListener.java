package games.sparking.crystalguard.commands;

import games.sparking.crystalguard.utils.messages.CC;
import games.sparking.crystalguard.utils.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;

public class CommandListener implements Listener {

    public static CommandMap getCommandMap() {
        if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
            try {
                Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                return (CommandMap) commandMapField.get(Bukkit.getPluginManager());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @EventHandler
    public void onUnknownCommand(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().substring(1).split(" ");
        String cmd = args[0];

        CommandMap commandMap = getCommandMap();
        if (commandMap == null) return;

        Command command = commandMap.getCommand(cmd);
        if (command == null) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(CC.errorMsg(Messages.INVALID));
        }
    }


}
