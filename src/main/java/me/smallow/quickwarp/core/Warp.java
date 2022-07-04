package me.smallow.quickwarp.core;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@SerializableAs("Warp")
public record Warp(int slot, ItemStack item, Location target) implements ConfigurationSerializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warp warp = (Warp) o;
        return slot == warp.slot;
    }

    @Override
    public int hashCode() {
        return Objects.hash(slot);
    }

    @Override
    public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("slot", slot);
        result.put("item", item);
        result.put("target", target);
        return result;
    }

    public static Warp deserialize(Map<String, Object> map) {
        return new Warp((int)map.get("slot"), (ItemStack) map.get("item"), (Location) map.get("target"));
    }
}
