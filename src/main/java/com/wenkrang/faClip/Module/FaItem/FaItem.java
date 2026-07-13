package com.wenkrang.faClip.Module.FaItem;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 这是ItemStack的扩展物品类
 */
public class FaItem extends ItemStack {
    public ItemStack getItemStack() {
        return itemStack;
    }

    private final ItemStack itemStack;

    public FaItem(ItemStack item) {
        super(item);
        itemStack = item;
    }

    public FaItem(Material material) {
        super(new ItemStack(material));
        itemStack = this;
    }

    private final UUID id = UUID.randomUUID();

    public UUID getId() {
        return id;
    }

    public static class tagMgr {
        private final PersistentDataHolder holder;

        private final PersistentDataContainer container;

        private final Map<String, String> tagMap = new HashMap<>();

        private UUID id;

        private NamespacedKey namespacedKey;

        public UUID getId() {
            return id;
        }

        public tagMgr(Plugin plugin, PersistentDataHolder holder) {
            this.holder = holder;
            container = holder.getPersistentDataContainer();


        }

        public void init() {

        }


    }
}

