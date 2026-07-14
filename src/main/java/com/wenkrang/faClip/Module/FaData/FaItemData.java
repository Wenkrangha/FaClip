package com.wenkrang.faClip.Module.FaData;

import com.wenkrang.faClip.Module.FaMessage.Helper.i18nHelper;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class FaItemData extends FaData{
    private FaItemData(String node) {
        super("data/item/" + node);
    }

    public static NamespacedKey key;

    public static UUID initUUID(ItemStack itemStack) {
        UUID finalUUID = UUID.randomUUID();

        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        container.set(key, PersistentDataType.STRING, finalUUID.toString());

        itemStack.setItemMeta(itemMeta);

        return finalUUID;
    }

    public static FaItemData get(ItemStack itemStack) {
        if (FaData.plugin == null) {
            plugin = detectCallingPlugin();
        }

        if (plugin == null) throw new NullPointerException(
                i18nHelper.t("FaData.Exception.FaYamlData.PluginIsNotinitialized")
        );

        key = new NamespacedKey(FaData.plugin, "FaItemData");

        // 获取持久化储存
        PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();

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
