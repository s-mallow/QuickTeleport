package me.smallow.quickwarp.commands.subs;

import me.smallow.quickwarp.QuickWarp;
import me.smallow.quickwarp.core.Warp;
import me.smallow.quickwarp.core.WarpManager;
import me.smallow.quickwarp.commands.SubInventoryCommand;
import me.smallow.quickwarp.core.QWInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RenameCommand extends SubInventoryCommand {

    private final Map<HumanEntity, String> addName;
    private final QWInventoryHolder inventoryHolder;
    private final ItemStack[] firstPageContent;

    public RenameCommand(WarpManager warpManager, QuickWarp plugin) {
        super(warpManager, plugin);

        addName = new HashMap<>();
        firstPageContent = new ItemStack[54];
        for(Warp warp: warpManager.getWarps()){
            firstPageContent[warp.slot()] = warp.item();
        }
        inventoryHolder = new QWInventoryHolder("Select the Warp to rename", firstPageContent);
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
        if(args.length < 1){
            p.sendMessage(ChatColor.RED + "Usage: /" + label + " rename <name>");
            return;
        }
        addName.put(p, ChatColor.RESET +
                ChatColor.translateAlternateColorCodes('&', String.join(" ", args)));
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
                    Warp oldWarp = warpManager.removeWarp(event.getSlot());
                    ItemStack item = oldWarp.item();
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(addName.get(event.getWhoClicked()));
                    item.setItemMeta(meta);
                    Warp newWarp = new Warp(oldWarp.slot(), item, oldWarp.target());
                    warpManager.addWarp(newWarp);
                } catch (WarpManager.WarpManagerException e) {
                    event.getWhoClicked().sendMessage(ChatColor.RED + e.getMessage());
                    return;
                }
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "Warp renamed.");
            });
        }
    }
}
