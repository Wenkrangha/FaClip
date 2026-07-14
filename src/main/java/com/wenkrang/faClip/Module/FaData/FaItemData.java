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

    /**
     * 获取标准FaItem的NamespacedKey
     * @return 标准FaItem的NamespacedKey
     */
    public static NamespacedKey getKey() {
        if (FaData.plugin == null) {
            plugin = detectCallingPlugin();
        }

        if (plugin == null) throw new NullPointerException(
                i18nHelper.t("FaData.Exception.FaYamlData.PluginIsNotinitialized")
        );

        return new NamespacedKey(FaData.plugin, "FaItemData");
    }

    /**
     * 创建一个FaItemData
     * 注意：当一个物品第一次被创建FaItemData时，会为该物品生成一个UUID，
     *      并保存在物品的持久化储存中，它将不能与其他普通物品堆叠
     * @param itemStack 需要创建FaItemData的ItemStack
     * @return 创建的FaItemData
     */
    public static FaItemData create(ItemStack itemStack) {
        key = getKey();

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

    public static boolean has(ItemStack itemStack) {
        PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        String s = persistentDataContainer.get(getKey(), PersistentDataType.STRING);

        return s != null;
    }
}
