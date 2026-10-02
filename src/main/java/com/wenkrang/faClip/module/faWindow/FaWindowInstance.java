package com.wenkrang.faClip.module.faWindow;

import com.wenkrang.faClip.helper.ResourceHelper;
import com.wenkrang.faClip.module.FaModule;
import com.wenkrang.faClip.module.faData.FaInventoryData;
import com.wenkrang.faClip.module.faInterface.FaInterfaceInstance;
import com.wenkrang.faClip.module.faItem.FaItemInstance;
import com.wenkrang.faClip.module.faWindow.event.*;
import com.wenkrang.faClip.module.faWindow.interpreter.FaInvInterpreter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wenkrang.faClip.module.faWindow.helper.WinDataHelper.getDesignArray;

public class FaWindowInstance implements FaModule {
    private final Plugin plugin;

    private final FaItemInstance faItemInstance;

    private final FaInvInterpreter faInvInterpreter;

    private final FaInterfaceInstance faInterfaceInstance;

    private final Map<String, FaInventory> inventories = new HashMap<>();

    private final ResourceHelper resourceHelper;

    private Status status = Status.STOPPED;

    public FaWindowInstance(Plugin p,FaItemInstance f) {
        plugin = p;
        faItemInstance = f;
        faInvInterpreter = new FaInvInterpreter(this);
        faInterfaceInstance = new FaInterfaceInstance(plugin);
        resourceHelper = new ResourceHelper(plugin.getClass());

        registerEvents();
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
        inventories.clear();
        status = Status.STOPPED;
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public String getName() {
        return "FaWindow";
    }

    public void autoRegister() {
        faInterfaceInstance.enableForAll(plugin);
    }

    public void register(Class<?>... clazz) {
        faInterfaceInstance.enableFor(clazz);
    }

    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }
    public Plugin getPlugin() {
        return plugin;
    }

    public FaItemInstance getFaItemInstance() {
        return faItemInstance;
    }

    public FaInterfaceInstance getFaInterfaceInstance() {
        return faInterfaceInstance;
    }

    public void load(String path) {
        FaInventory inv = faInvInterpreter.interpreter(path);

        inventories.putIfAbsent(inv.id, inv);
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new InvLockE(this), plugin);
        Bukkit.getPluginManager().registerEvents(new InvInitE(this), plugin);
        Bukkit.getPluginManager().registerEvents(new InvCloseE(this), plugin);
        Bukkit.getPluginManager().registerEvents(new InvClickE(this), plugin);
        Bukkit.getPluginManager().registerEvents(new InvRefE(this), plugin);
        Bukkit.getPluginManager().registerEvents(new InvBackRefE(this), plugin);
    }

    public void loadAll() {
        List<String> inv = resourceHelper.getResourcesByExtension("inv");

        for (String s : inv) {
            load(s);
        }
    }


    public FaInvInterpreter getFaInvInterpreter() {
        return faInvInterpreter;
    }

    public @Nullable FaInventory getFaInventory(String id) {
        FaInventory faInventory = inventories.get(id);

        if (faInventory != null) {
            return faInventory.clone();
        }

        return null;
    }

    /**
     * 设置界面数据
     * @param inv 界面
     * @param data 界面数据
     */
    public void setData(FaInventory inv, FaInventoryData data, FaWindow faWindow) {
        // 基本信息
        data.set("id", inv.id);
        data.set("name", inv.name);

        // 设计
        data.set("design", inv.getDesign());
        // 定义
        data.set("define", inv.getDefine());

        // 功能
        data.set("lock", inv.lock);

        // 应用事件
        for (Map.Entry<String, String> entry : inv.getEvents().entrySet()) {
            data.set("event." + entry.getKey(), entry.getValue());
        }

        // 应用标签
        data.set("note", inv.getAllNote());

        // 设置可动格
        data.set("moveable", inv.getMoveableSlots());

        data.set("win", faWindow);
    }



    /**
     * 渲染界面
     * @return 返回
     */
    public Inventory render(FaInventory inv) {
        // 从初始化数据
        FaInventoryData faInventoryData = new FaInventoryData();
        Inventory inventory = Bukkit.createInventory(faInventoryData, inv.size, inv.name);
        faInventoryData.setInventory(inventory);

        char[] charArray = getDesignArray(inv.getDesign());

        // 应用定义流
        for (int i = 0;i < charArray.length;i++) {
            char c = charArray[i];

            ItemStack itemStack = inv.getDefine(String.valueOf(c));

            if (itemStack != null) inventory.setItem(i, itemStack.clone());
        }

        // 覆盖物品
        inv.getOverride().forEach(inventory::setItem);

        return inventory;
    }
}
