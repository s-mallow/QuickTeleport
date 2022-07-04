package me.smallow.quickwarp;

import me.smallow.quickwarp.commands.CommandManager;
import me.smallow.quickwarp.core.Warp;
import me.smallow.quickwarp.core.WarpManager;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class QuickWarp extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(Warp.class, "Warp");
        File databasefile = new File(getDataFolder() + "/database.yml");
        if (!databasefile.exists()) {
            databasefile.getParentFile().mkdirs();
            saveResource("database.yml", false);
        }
        WarpManager manager = new WarpManager(databasefile);
        getCommand("quickwarp").setExecutor(new CommandManager(manager, this));

        getLogger().log(Level.INFO, "QuickWarp enabled");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "QuickWarp disabled");
    }
}
