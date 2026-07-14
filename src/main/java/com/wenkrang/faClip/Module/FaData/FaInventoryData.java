package com.wenkrang.faClip.Module.FaData;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FaInventoryData implements InventoryHolder {

    private final Map<String,String> data = new HashMap<>();

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    private Inventory inventory;

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void set(String key,String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public void remove(String key) {
        data.remove(key);
    }

    public void clear() {
        data.clear();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    public void putAll(Map<String,String> map) {
        data.putAll(map);
    }

    public Map<String,String> getAll() {
        return data;
    }

    public boolean has(String key) {
        return data.containsKey(key);
    }

}
