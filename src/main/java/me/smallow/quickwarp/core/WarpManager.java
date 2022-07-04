package me.smallow.quickwarp.core;

import me.smallow.quickwarp.commands.SubInventoryCommand;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WarpManager {

    private final List<Warp> warps;
    private final YamlConfiguration config;
    private final File configFile;
    private final WarpInventoryNotifier inventoryNotifier;

    public WarpManager(File configFile) {
        this.configFile = configFile;
        this.config = YamlConfiguration.loadConfiguration(configFile);
        this.warps = (List<Warp>) config.getList("warps", new ArrayList<>());
        this.inventoryNotifier = new WarpInventoryNotifier();
    }

    public List<Warp> getWarps() {
        return new ArrayList<>(warps);
    }

    public void addWarp(Warp warp) throws WarpManagerException {
        if (warps.contains(warp)) {
            throw new WarpManagerException("A Warp already exists in that slot");
        }
        warps.add(warp);
        setWarpsAndSave();
        inventoryNotifier.notifyItemChange(warp.slot(), warp.item());
    }

    public Warp removeWarp(int slot) throws WarpManagerException {
        Warp warp = warps.stream().filter(w -> w.slot() == slot).findFirst()
                .orElseThrow(() -> new WarpManagerException("No Warp exists in that slot"));
        warps.remove(warp);
        setWarpsAndSave();
        inventoryNotifier.notifyItemChange(slot, null);
        return warp;
    }

    private void setWarpsAndSave() throws WarpManagerException {
        config.set("warps", warps);
        try {
            config.save(configFile);
        } catch (IOException e) {
            throw new WarpManagerException("Error saving config");
        }
    }

    public static class WarpManagerException extends Exception {
        public WarpManagerException(String message) {
            super(message);
        }
    }

    public void registerListener(SubInventoryCommand listener) {
        inventoryNotifier.registerListener(listener);
    }

    public void unregisterListener(SubInventoryCommand listener) {
        inventoryNotifier.unregisterListener(listener);
    }

}