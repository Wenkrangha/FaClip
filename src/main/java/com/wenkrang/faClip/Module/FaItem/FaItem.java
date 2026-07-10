package com.wenkrang.faClip.Module.FaItem;

import org.bukkit.inventory.ItemStack;

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


}
