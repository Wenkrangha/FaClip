package com.wenkrang.faClip.Module.FaCommand;

import com.wenkrang.faClip.Module.FaCommand.FaCmdInterpreter.FaCmdInterpreter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;

/**
 * FaCmd 命令封装类
 * 用于存储和管理单个命令的所有属性和配置信息
 */
public class FaCmd {
    /** 所属插件 */
    private Plugin plugin;
    /** 命令名称 */
    private String name;
    /** 命令标签 */
    private String label;
    /** 激活的别名列表 */
    private List<String> activeAliases;
    /** 命令映射实例 */
    private CommandMap commandMap;
    /** 命令描述 */
    protected String description;
    /** 权限节点 */
    private String permission;
    /** 命令解释器 */
    private FaCmdInterpreter interpreter;
    /** 命令节点路径（如：plugin.command.subcommand） */
    private String node;
    /** 关联的执行方法 */
    private Method method;
    /** 是否需要 OP 权限 */
    private boolean requireOP;
    /** Bukkit 命令对象 */
    private Command command;
    /** 是否只为帮助命令 **/
    private boolean isOnlyForHelp;

    public boolean isOnlyForHelp() {
        return isOnlyForHelp;
    }

    public void setOnlyForHelp(boolean onlyForHelp) {
        isOnlyForHelp = onlyForHelp;
    }

    public String getHelp() {
        return Help;
    }

    public void setHelp(String help) {
        Help = help;
    }

    /** 命令帮助信息 */
    private String Help;

    /**
     * 构造函数，初始化 FaCmd 实例
     * 
     * @param faCmdInterpreter 命令解释器实例
     */
    public FaCmd(@NotNull FaCmdInterpreter faCmdInterpreter) {
        interpreter = faCmdInterpreter;
        commandMap = faCmdInterpreter.getFaCmdInstance().getCommandManager().getCommandMap();
    }

    /**
     * 设置命令关联的执行方法
     * 
     * @param m 执行方法
     */
    public void setMethod(Method m) {method = m;}

    /**
     * 获取命令关联的执行方法
     * 
     * @return 执行方法
     */
    public Method getMethod() {
        return method;
    }

    /**
     * 获取命令节点路径
     * 
     * @return 命令节点
     */
    public String getNode() {
        return node;
    }

    /**
     * 设置命令节点路径
     * 
     * @param node 命令节点
     */
    public void setNode(String node) {
        this.node = node;
    }

    /**
     * 获取命令解释器
     * 
     * @return 命令解释器
     */
    public FaCmdInterpreter getInterpreter() {
        return interpreter;
    }

    /**
     * 设置命令解释器
     * 
     * @param interpreter 命令解释器
     */
    public void setInterpreter(FaCmdInterpreter interpreter) {
        this.interpreter = interpreter;
    }

    /**
     * 获取 Bukkit 命令对象
     * 
     * @return Bukkit 命令对象
     */
    public Command getCommand() {
        return command;
    }

    /**
     * 设置 Bukkit 命令对象
     * 
     * @param command Bukkit 命令对象
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * 获取命令名称
     * 
     * @return 命令名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置命令名称
     * 
     * @param name 命令名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取命令标签
     * 
     * @return 命令标签
     */
    public String getLabel() {
        return label;
    }

    /**
     * 设置命令标签
     * 
     * @param label 命令标签
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * 获取激活的别名列表
     * 
     * @return 别名列表
     */
    public List<String> getActiveAliases() {
        return activeAliases;
    }

    /**
     * 设置激活的别名列表
     * 
     * @param activeAliases 别名列表
     */
    public void setActiveAliases(List<String> activeAliases) {
        this.activeAliases = activeAliases;
    }

    /**
     * 获取命令映射实例
     * 
     * @return 命令映射实例
     */
    public CommandMap getCommandMap() {
        return commandMap;
    }

    /**
     * 设置命令映射实例
     * 
     * @param commandMap 命令映射实例
     */
    public void setCommandMap(CommandMap commandMap) {
        this.commandMap = commandMap;
    }

    /**
     * 获取命令描述
     * 
     * @return 命令描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置命令描述
     * 
     * @param description 命令描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取权限节点
     * 
     * @return 权限节点
     */
    public String getPermission() {
        return permission;
    }

    /**
     * 设置权限节点
     * 
     * @param permission 权限节点
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * 检查是否需要 OP 权限
     * 
     * @return 如果需要 OP 权限返回 true
     */
    public boolean isRequireOP() {
        return requireOP;
    }

    /**
     * 设置是否需要 OP 权限
     * 
     * @param requireOP 是否需要 OP 权限
     */
    public void setRequireOP(boolean requireOP) {
        this.requireOP = requireOP;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
}
