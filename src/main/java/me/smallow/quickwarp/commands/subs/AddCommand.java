package me.smallow.quickwarp.commands.subs;

import me.smallow.quickwarp.QuickWarp;
import me.smallow.quickwarp.commands.SubInventoryCommand;
import me.smallow.quickwarp.core.QWInventoryHolder;
import me.smallow.quickwarp.core.Warp;
import me.smallow.quickwarp.core.WarpManager;
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

public class AddCommand extends SubInventoryCommand {

    private final static ItemStack ADD_ITEM = new ItemStack(Material.LIME_WOOL);

    private final Map<HumanEntity, ItemStack> addItem;
    private final QWInventoryHolder inventoryHolder;
    private final ItemStack[] firstPageContent;

    static {
        ItemMeta meta = ADD_ITEM.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Add Warp");
        ADD_ITEM.setItemMeta(meta);
    }

    public AddCommand(WarpManager warpManager, QuickWarp plugin) {
        super(warpManager, plugin);

        addItem = new HashMap<>();
        firstPageContent = new ItemStack[54];
        for(Warp warp: warpManager.getWarps()){
            firstPageContent[warp.slot()] = warp.item();
        }
        for(int i = 0; i < firstPageContent.length; i++){
            if(firstPageContent[i] == null){
                firstPageContent[i] = ADD_ITEM;
            }
        }
        inventoryHolder = new QWInventoryHolder("Select where to add the Warp", firstPageContent);
    }

    @Override
    public void notifyItemChange(int slot, ItemStack item) {
        firstPageContent[slot] = item == null || item.getType() == Material.AIR ? ADD_ITEM : item;
        inventoryHolder.setContents(firstPageContent);
    }

    @Override
    public void notifySizeChange(int size) {
        inventoryHolder.setContents(Arrays.copyOf(firstPageContent, size));
    }

    @Override
    public void execute(Player p, String label, String[] args) {
        if(args.length < 1){
            p.sendMessage(ChatColor.RED + "Usage: /" + label + " add <name>");
            return;
        }
        if(p.getInventory().getItemInMainHand().getType() == Material.AIR){
            p.sendMessage(ChatColor.RED + "You must hold an item in your hand to add a warp.");
            return;
        }
        ItemStack item = p.getInventory().getItemInMainHand().clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET +
                ChatColor.translateAlternateColorCodes('&', String.join(" ", args)));
        item.setItemMeta(meta);
        addItem.put(p, item);
        p.openInventory(inventoryHolder.getInventory());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWarpInventoryClick(InventoryClickEvent event) {
        if(inventoryHolder.equals(event.getInventory().getHolder())) {
            event.setCancelled(true);
            if(!event.getInventory().equals(event.getClickedInventory()) || event.getCurrentItem() == null){
                return;
            }
            Bukkit.getScheduler().runTask(plugin, () -> {
                Player p = (Player) event.getWhoClicked();
                Warp warp = new Warp(event.getSlot(), addItem.get(p), event.getWhoClicked().getLocation());
                try {
                    warpManager.addWarp(warp);
                    addItem.remove(p);
                } catch (WarpManager.WarpManagerException e) {
                    p.sendMessage(ChatColor.RED + e.getMessage());
                    return;
                }
                p.sendMessage(ChatColor.GREEN + "Warp added.");
                p.closeInventory();
            });
        }
    }
}
