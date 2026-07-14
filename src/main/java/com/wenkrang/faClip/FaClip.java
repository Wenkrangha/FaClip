package com.wenkrang.faClip;

import com.wenkrang.faClip.Module.FaCommand.FaCmdInstance;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * FaClip 主插件类
 * 负责插件的启动、关闭以及命令注册
 */
public final class FaClip extends JavaPlugin {

    /** 命令实例管理器 */
    FaCmdInstance faCmdInstance;

    /**
     * 插件启用时调用
     * 初始化命令实例并注册所有命令
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
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
