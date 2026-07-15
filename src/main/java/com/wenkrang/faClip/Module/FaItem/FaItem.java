package com.wenkrang.faClip.Module.FaItem;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * 这是ItemStack的扩展物品类
 */
public class FaItem extends ItemStack {
    public Plugin plugin;

    public FaItem(Plugin p,ItemStack item) {
        super(item);
        plugin = p;
    }

    public FaItem(Plugin p,Material material) {
        super(new ItemStack(material));
        plugin = p;
    }

    private NamespacedKey namespacedKey;

    public void setNamespacedKey(NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
    }

    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public tagMgr getTagMgr() {
        return new tagMgr(plugin,this);
    }

}

