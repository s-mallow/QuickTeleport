package me.smallow.quickteleport;

import me.smallow.quickteleport.commands.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class QuickTeleport extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("quickteleport").setExecutor(new CommandManager());
        getLogger().log(Level.INFO, "QuickTeleport enabled");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "QuickTeleport disabled");
    }
}
