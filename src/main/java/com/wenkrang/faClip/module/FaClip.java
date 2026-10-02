package com.wenkrang.faClip.module;

import com.wenkrang.faClip.module.faData.FaConfig;
import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faDebugger.Debugger;
import com.wenkrang.faClip.module.faMessage.Fm;
import com.wenkrang.faClip.module.faMessage.helper.I18nHelper;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * FaClip 主插件类
 * 负责插件的启动、关闭以及命令注册
 */
public final class FaClip extends JavaPlugin {

    public static Plugin plugin;

    public static Debugger debugger;

    public static boolean debug = false;

    /**
     * 插件启用时调用
     * 初始化命令实例并注册所有命令
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        // 初始化数据访问
        FaData.init(FaClip.plugin);

        // 获取配置
        FaConfig faConfig = new FaConfig();
        debug = faConfig.getBoolean("enableDebugger");
        if (debug){
            debugger = new Debugger();
            Fm.info(I18nHelper.t("FaDebugger.Info.Debugger.Enable"));
        }

    }

    /**
     * 插件禁用时调用
     * 清理并注销所有注册的命令
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
