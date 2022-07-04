package me.smallow.quickwarp.commands;

import me.smallow.quickwarp.QuickWarp;
import me.smallow.quickwarp.core.WarpManager;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public abstract class SubInventoryCommand extends SubCommand implements Listener {

    protected final QuickWarp plugin;

    public SubInventoryCommand(WarpManager warpManager, QuickWarp plugin) {
        super(warpManager);
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.warpManager.registerListener(this);
    }

    public abstract void notifyItemChange(int slot, ItemStack item);

    public abstract void notifySizeChange(int size);
}
