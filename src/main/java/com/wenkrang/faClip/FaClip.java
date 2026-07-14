package com.wenkrang.faClip;

import com.wenkrang.faClip.Module.FaCommand.FaCmdInstance;
import com.wenkrang.faClip.Module.FaData.FaConfig;
import com.wenkrang.faClip.Module.FaData.FaData;
import com.wenkrang.faClip.Module.FaDebugger.Debugger;
import com.wenkrang.faClip.Module.FaMessage.Fm;
import com.wenkrang.faClip.Module.FaMessage.Helper.i18nHelper;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * FaClip 主插件类
 * 负责插件的启动、关闭以及命令注册
 */
public final class FaClip extends JavaPlugin {

    /** 命令实例管理器 */
    FaCmdInstance faCmdInstance;

    public static Plugin plugin;

    public static Debugger debugger;

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
        System.out.println(faConfig.getFile());
        if (faConfig.getBoolean("enableDebugger")){
            debugger = new Debugger();
            Fm.info(i18nHelper.t("FaDebugger.Info.Debugger.Enable"));
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
