package me.smallow.quickwarp.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class QWInventoryHolder implements InventoryHolder {

    private Inventory inventory;
    private String title;

    public QWInventoryHolder(String title, ItemStack[] content) {
        this.inventory = Bukkit.createInventory(this, content.length, title);
        this.inventory.setContents(content);
        this.title = title;
    }

    public void setTitle(String title){
        ItemStack[] content = inventory.getContents();
        List<HumanEntity> viewers = inventory.getViewers();

        inventory = Bukkit.createInventory(this, inventory.getSize(), title);
        viewers.forEach(v -> v.openInventory(inventory));
        inventory.setContents(content);
        this.title = title;
    }

    public void setContents(ItemStack[] content){
        if(content.length != inventory.getSize()){
            List<HumanEntity> viewers = inventory.getViewers();

            inventory = Bukkit.createInventory(this, content.length, this.title);
            viewers.forEach(v -> v.openInventory(inventory));
        }
        inventory.setContents(content);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
