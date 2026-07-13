package com.wenkrang.faClip.Module.FaData;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.ListPersistentDataTypeProvider;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.UUID;

public class FaItemData extends FaData{
    private FaItemData(String node) {
        super("data/item/" + node);
    }

    public static PersistentDataContainer getContainer(ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer();
    }

    public static NamespacedKey key = new NamespacedKey(FaData.plugin, "FaItemData");

    public static UUID initUUID(ItemStack itemStack) {
        UUID finalUUID = UUID.randomUUID();

        PersistentDataContainer container = getContainer(itemStack);

        container.set(key, PersistentDataType.STRING, finalUUID.toString());

        return finalUUID;
    }

    public static FaItemData create(ItemStack itemStack) {
        // 获取持久化储存
        PersistentDataContainer persistentDataContainer = getContainer(itemStack);

        // 临时UUID
        UUID finalUUID;

        //这里防止UUID获取失败
        try {
            String s = persistentDataContainer.get(key, PersistentDataType.STRING);
            if (s != null) {
                finalUUID = UUID.fromString(s);
            } else {
                finalUUID = initUUID(itemStack);
            }
        }catch (Exception e) {
            finalUUID = initUUID(itemStack);
        }

        return new FaItemData(finalUUID.toString());
    }
}
