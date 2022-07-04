package me.smallow.quickwarp.commands.subs;

import me.smallow.quickwarp.QuickWarp;
import me.smallow.quickwarp.core.Warp;
import me.smallow.quickwarp.core.WarpManager;
import me.smallow.quickwarp.commands.SubInventoryCommand;
import me.smallow.quickwarp.core.QWInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

public class SetItemCommand extends SubInventoryCommand {

    private final Map<HumanEntity, ItemStack> addItem;
    private final QWInventoryHolder inventoryHolder;
    private final ItemStack[] firstPageContent;

    public SetItemCommand(WarpManager warpManager, QuickWarp plugin) {
        super(warpManager, plugin);

        addItem = new HashMap<>();
        firstPageContent = new ItemStack[54];
        for(Warp warp: warpManager.getWarps()){
            firstPageContent[warp.slot()] = warp.item();
        }
        inventoryHolder = new QWInventoryHolder("Select the Warp to change", firstPageContent);
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
        if(p.getInventory().getItemInMainHand().getType() == Material.AIR){
            p.sendMessage(ChatColor.RED + "You must hold an item in your hand to add a warp.");
            return;
        }
        addItem.put(p, p.getInventory().getItemInMainHand().clone());
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
                    ItemStack item = addItem.get(event.getWhoClicked());
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.RESET + oldWarp.item().getItemMeta().getDisplayName());
                    item.setItemMeta(meta);
                    Warp newWarp = new Warp(oldWarp.slot(), item.clone(), oldWarp.target());
                    warpManager.addWarp(newWarp);
                } catch (WarpManager.WarpManagerException e) {
                    event.getWhoClicked().sendMessage(ChatColor.RED + e.getMessage());
                    return;
                }
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "Item changed.");
            });
        }
    }
}
