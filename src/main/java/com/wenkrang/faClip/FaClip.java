package com.wenkrang.faClip;

import com.wenkrang.faClip.Module.FaCommand.Annotation.Cmd;
import com.wenkrang.faClip.Module.FaCommand.Annotation.Help;
import com.wenkrang.faClip.Module.FaCommand.Annotation.OnlyForHelp;
import com.wenkrang.faClip.Module.FaCommand.FaCmdInstance;
import com.wenkrang.faClip.Module.FaCommand.FaCmdInterpreter.FaCmdContext;
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
        faCmdInstance = FaCmdInstance.create(this);
        faCmdInstance.enableForAll(this);
    }

    /**
     * 插件禁用时调用
     * 清理并注销所有注册的命令
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        faCmdInstance.close();
    }

    @Cmd("testmain")
    @OnlyForHelp
    @Help("测试")
    public static void test() {}

    @Cmd("testmain.A")
    @Help("测试A")
    public static void A(FaCmdContext faCmdContext){
        faCmdContext.sender().sendMessage("a");
    }

    @Cmd("testmain.B")
    @Help("测试B")
    public static void B(){}
}
