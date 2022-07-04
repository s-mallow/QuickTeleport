package me.smallow.quickwarp.commands.subs;

import me.smallow.quickwarp.QuickWarp;
import me.smallow.quickwarp.core.WarpManager;
import me.smallow.quickwarp.commands.SubInventoryCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RemoveCommand extends SubInventoryCommand {

    public RemoveCommand(WarpManager warpManager, QuickWarp plugin) {
        super(warpManager, plugin);
    }

    @Override
    public void notifyItemChange(int slot, ItemStack item) {

    }

    @Override
    public void notifySizeChange(int size) {

    }

    @Override
    public void execute(Player p, String label, String[] args) {

    }
}
