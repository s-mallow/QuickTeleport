package me.smallow.quickwarp.commands.subs;

import me.smallow.quickwarp.QuickWarp;
import me.smallow.quickwarp.core.Warp;
import me.smallow.quickwarp.core.WarpManager;
import me.smallow.quickwarp.commands.SubInventoryCommand;
import me.smallow.quickwarp.core.QWInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class RemoveCommand extends SubInventoryCommand {

    private final QWInventoryHolder inventoryHolder;
    private final ItemStack[] firstPageContent;

    public RemoveCommand(WarpManager warpManager, QuickWarp plugin) {
        super(warpManager, plugin);
        firstPageContent = new ItemStack[54];
        for(Warp warp: warpManager.getWarps()){
            firstPageContent[warp.slot()] = warp.item();
        }
        inventoryHolder = new QWInventoryHolder("Select the Warp to remove", firstPageContent);
    }

    @Override
    public void notifyItemChange(int slot, ItemStack item) {
        firstPageContent[slot] = item;
        inventoryHolder.setContents(firstPageContent);
    }

    @Override
    public void notifySizeChange(int size) {
        inventoryHolder.setContents(Arrays.copyOf(firstPageContent, size));
    }

    @Override
    public void execute(Player p, String label, String[] args) {
        p.openInventory(inventoryHolder.getInventory());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRemoveInventoryClick(InventoryClickEvent event) {
        if(inventoryHolder.equals(event.getInventory().getHolder())) {
            event.setCancelled(true);
            if(!event.getInventory().equals(event.getClickedInventory()) || event.getCurrentItem() == null){
                return;
            }
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    warpManager.removeWarp(event.getSlot());
                } catch (WarpManager.WarpManagerException e) {
                    event.getWhoClicked().sendMessage(ChatColor.RED + e.getMessage());
                    return;
                }
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "Warp removed.");
            });
        }
    }
}
