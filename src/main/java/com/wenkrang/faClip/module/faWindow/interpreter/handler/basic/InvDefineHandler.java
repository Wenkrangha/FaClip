package com.wenkrang.faClip.module.faWindow.interpreter.handler.basic;

import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faWindow.FaInventory;
import com.wenkrang.faClip.module.faWindow.FaWindowInstance;
import com.wenkrang.faClip.module.faWindow.interpreter.handler.FaInvHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class InvDefineHandler implements FaInvHandler {


    @Override
    public void handle(FaInventory faInventory, FaData faData, FaWindowInstance faWindowInstance) {
        ConfigurationSection define = faData.getSection("define");

        if (define != null) {
            Set<String> keys = define.getKeys(false);

            for (String key : keys) {
                String value = define.getString(key);

                // 转换物品
                ItemStack itemStack = faWindowInstance.getFaItemInstance().convertDefine(value);

                faInventory.define(key, itemStack);
            }
        }
    }
}
