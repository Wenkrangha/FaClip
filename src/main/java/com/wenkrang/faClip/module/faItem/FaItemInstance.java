package com.wenkrang.faClip.module.faItem;

import com.wenkrang.faClip.helper.ResourceHelper;
import com.wenkrang.faClip.module.FaModule;
import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faInterface.FaInterfaceInstance;
import com.wenkrang.faClip.module.faItem.interpreter.FaItemInterpreter;
import com.wenkrang.faClip.module.faItem.event.FaItemClickE;
import com.wenkrang.faClip.module.faItem.event.FaItemInvClickE;
import com.wenkrang.faClip.module.faItem.event.FaItemIsolateE;
import com.wenkrang.faClip.module.faItem.interpreter.helper.ItemDataHelper;
import com.wenkrang.faClip.module.faMessage.Fm;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FaItem 实例管理器，负责加载、存储和检索自定义 FaItem 对象
 * 内部通过命名空间键值对维护所有已注册的 FaItem
 */
public class FaItemInstance implements FaModule {
    // 以命名空间为键、FaItem 为值的注册表
    private final Map<String,FaItem> faItems;

    private final Plugin plugin;

    private final FaItemInterpreter faItemInterpreter;

    private final FaInterfaceInstance faInterfaceInstance;

    private Status status = Status.STOPPED;
    
    /**
     * 构造一个 FaItemInstance 实例
     * @param p 所属插件实例，用于读取插件内部资源
     */
    public FaItemInstance(Plugin p) {
        this.faItems = new HashMap<>();
        plugin = p;
        faItemInterpreter = new FaItemInterpreter(plugin);
        faInterfaceInstance = new FaInterfaceInstance(plugin);

        Bukkit.getPluginManager().registerEvents(new FaItemClickE(plugin, this), plugin);
        Bukkit.getPluginManager().registerEvents(new FaItemInvClickE(plugin, this), plugin);
        Bukkit.getPluginManager().registerEvents(new FaItemIsolateE(this), plugin);
    }

    @Override
    public void auto() {
        status = Status.STARTING;

        loadAll();
        autoRegister();

        status = Status.READY;
    }

    @Override
    public void close() {
        faItems.clear();
        status = Status.STOPPED;
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public String getName() {
        return "FaItem";
    }

    public void autoRegister() {
        faInterfaceInstance.enableForAll(plugin);
    }

    public void registerHandler(Class<?>... clazz) {
        faInterfaceInstance.enableFor(clazz);
    }

    public FaInterfaceInstance getFaInterfaceInstance() {
        return faInterfaceInstance;
    }

    /**
     * 根据键名获取已注册的 FaItem
     * @param key 命名空间键名
     * @return 对应的 FaItem 对象，若不存在则返回 null
     */
    public @Nullable FaItem getFaItem(String key) {
        FaItem faItem = faItems.get(key);

        return faItem == null ? null : faItem.copy();
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

        // 通过解释器将 FaData 解析为 FaItem 对象
        FaItem item = faItemInterpreter.interpreter(faData);
        
        // 以物品的命名空间为键，注册到内部注册表
        faItems.put(ItemDataHelper.getID(item), item);
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

    public Map<String,FaItem> getAll() {
        return faItems;
    }

    public @Nullable ItemStack convertDefine(String value) {
        ItemStack item = null;

        if (value != null) {
            // 获取数量
            int amount = 1;
            String defineName = value;
            if (value.contains("*")) {
                amount = Integer.parseInt(value.split("\\*")[1]);
                defineName = value.split("\\*")[0];
            }

            // 获取物品
            if (defineName.startsWith("MC.")) {
                item = new ItemStack(Material.valueOf(defineName.replace("MC.", "")));
            }else {
                FaItemInstance faItemInstance = this;

                FaItem faItem = faItemInstance.getFaItem(defineName);

                if (faItem != null) {
                    item = faItem.copy();
                }else {
                    Fm.warning("define 引用了不存在的物品: " + value);
                }
            }

            if (item != null) {
                item.setAmount(amount);
            }
        }

        return item;
    }

    /**
     * 根据组名获取所有 FaItem
     * @param key 组名
     * @return 所有 FaItem
     */
    public List<FaItem> getGroup(String key) {
        return faItems.values().stream()
                .filter(item -> item.getGroup().contains(key))
                .map(FaItem::copy)
                .toList();
    }
}
