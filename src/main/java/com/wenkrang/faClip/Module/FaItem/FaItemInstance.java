package com.wenkrang.faClip.Module.FaItem;

import com.wenkrang.faClip.Helper.ResourceHelper;
import com.wenkrang.faClip.Module.FaData.FaData;
import com.wenkrang.faClip.Module.FaItem.FaItemInterpreter.FaItemInterpreter;
import com.wenkrang.faClip.Module.FaMessage.Fm;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FaItem 实例管理器，负责加载、存储和检索自定义 FaItem 对象
 * 内部通过命名空间键值对维护所有已注册的 FaItem
 */
public class FaItemInstance {
    // 以命名空间为键、FaItem 为值的注册表
    private final Map<String,FaItem> faItems;

    private Plugin plugin;

    private FaItemInterpreter faItemInterpreter;
    
    /**
     * 构造一个 FaItemInstance 实例
     * @param p 所属插件实例，用于读取插件内部资源
     */
    public FaItemInstance(Plugin p) {
        this.faItems = new HashMap<>();
        plugin = p;
        faItemInterpreter = new FaItemInterpreter(plugin);
    }

    /**
     * 根据键名获取已注册的 FaItem
     * @param key 命名空间键名
     * @return 对应的 FaItem 对象，若不存在则返回 null
     */
    public FaItem getFaItem(String key) {
        return faItems.get(key);
    }

    /**
     * 手动注册一个 FaItem 到指定键名
     * @param key 命名空间键名
     * @param faItem 要注册的 FaItem 对象
     */
    public void setFaItem(String key, FaItem faItem) {
        faItems.put(key, faItem);
    }

    /**
     * 从插件资源中加载单个物品定义文件，并将其注册到内部注册表
     * @param path 插件资源路径（相对于插件 JAR 内部路径）
     */
    public void load(String path) {
        // 从插件 JAR 中读取指定路径的资源流
        InputStream resource = plugin.getResource(path);

        // 将资源流解析为 FaData，提取 YAML 配置
        FaData faData = new FaData(resource);

        // 通过解释器将 YAML 节点解析为 FaItem 对象
        FaItem item = faItemInterpreter.interpreter(faData.getYaml());
        
        // 以物品的命名空间为键，注册到内部注册表
        faItems.put(item.getNamespacedKey().getKey(), item);
    }
    
    /**
     * 扫描插件资源中所有 .item 后缀的文件并批量加载
     */
    public void loadAll() {
        ResourceHelper resourceHelper = new ResourceHelper(plugin.getClass());

        // 获取插件 JAR 内所有 .item 扩展名的资源路径列表
        List<String> items = resourceHelper.getResourcesByExtension("item");
        
        for (String item : items) {
            load(item);
        }
    }

    /**
     * 判断物品是否等于指定 ID
     *
     * @param itemStack 要判断的物品
     * @param id        要判断的 ID
     * @return 是否等于指定 ID
     */
    public boolean equals(@NotNull ItemStack itemStack, String id) {
        if (itemStack.getItemMeta() == null) return false;

        tagMgr tagMgr = new tagMgr(plugin, itemStack);

        return tagMgr.has("id") && id.equals(tagMgr.get("id"));
    }

    public Map<String,FaItem> getAll() {
        return faItems;
    }
}
