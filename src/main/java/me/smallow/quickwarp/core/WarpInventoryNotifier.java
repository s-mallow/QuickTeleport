package me.smallow.quickwarp.core;

import me.smallow.quickwarp.commands.SubInventoryCommand;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

class WarpInventoryNotifier {

    private final List<SubInventoryCommand> warpChangeInventoryListeners;

    protected WarpInventoryNotifier() {
        warpChangeInventoryListeners = new ArrayList<>();
    }

    protected void registerListener(SubInventoryCommand listener) {
        warpChangeInventoryListeners.add(listener);
    }

    protected void notifyItemChange(int slot, ItemStack item) {
        for (SubInventoryCommand listener : warpChangeInventoryListeners) {
            listener.notifyItemChange(slot, item);
        }
    }

    protected void notifySizeChange(int size) {
        for (SubInventoryCommand listener : warpChangeInventoryListeners) {
            listener.notifySizeChange(size);
        }
    }

    protected void unregisterListener(SubInventoryCommand listener) {
        warpChangeInventoryListeners.remove(listener);
    }
}
