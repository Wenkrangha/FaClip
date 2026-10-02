package com.wenkrang.faClip.module.faBootstrap;

import com.wenkrang.faClip.helper.PluginHelper;
import com.wenkrang.faClip.module.faCommand.FaCmdInstance;
import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faItem.FaItemInstance;
import com.wenkrang.faClip.module.faRecipe.FaRecipe;
import com.wenkrang.faClip.module.faRecipe.FaRecipeInstance;
import com.wenkrang.faClip.module.faWindow.FaWindowInstance;
import org.bukkit.plugin.Plugin;

public class Fa {
    private static FaCmdInstance faCmdInstance;
    private static FaItemInstance faItemInstance;
    private static FaWindowInstance faWindowInstance;
    private static FaRecipeInstance faRecipeInstance;

    public static FaCmdInstance cmd() {
        return faCmdInstance;
    }

    public static FaItemInstance item() {
        return faItemInstance;
    }

    public static FaWindowInstance win() {
        return faWindowInstance;
    }

    public static FaRecipeInstance recipe() {
        return faRecipeInstance;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    private static Plugin plugin;

    /**
     * 自动初始化
     */
    public static void auto() {
        plugin = PluginHelper.detectCallingPlugin();

        faCmdInstance = new FaCmdInstance(plugin);
        faItemInstance = new FaItemInstance(plugin);
        faWindowInstance = new FaWindowInstance(plugin, faItemInstance);
        faRecipeInstance = new FaRecipeInstance(plugin, faItemInstance);

        FaData.init(plugin);

        faCmdInstance.auto();
        faItemInstance.auto();
        faWindowInstance.auto();
        faRecipeInstance.auto();
    }
    /**
     * 关闭
     */
    public static void close() {
        faCmdInstance.close();
        faItemInstance.close();
        faWindowInstance.close();
        faRecipeInstance.close();
    }
}
