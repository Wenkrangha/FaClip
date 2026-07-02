package com.wenkrang.faClip.Manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static com.wenkrang.faClip.Moudle.FaMessage.Helper.i18nHelper.t;
import static com.wenkrang.faClip.Moudle.FaMessage.Fm.waring;

/**
 * CommandManager 类用于管理 Bukkit 插件中的命令注册、分发和补全功能。
 * 通过反射获取 Bukkit 的 CommandMap 实例，并提供一系列方法来操作命令。
 */
public class CommandManager {


    private CommandMap commandMap;
    private Field bukkitCommandMap;
    private Field knownCommandsField;

    /**
     * 构造函数，通过反射获取 Bukkit 的 CommandMap 实例。
     */
    public CommandManager() {
        try {
            // 获取 CommandMap
            bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
            // 允许访问
            knownCommandsField.setAccessible(true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            waring(t("FaCommand.Error.CommandMap.NotFound"));
            e.printStackTrace();
        }
    }

    /**
     * 获取 CommandMap 实例。
     *
     * @return CommandMap 实例
     */
    public CommandMap getCommandMap() {
        return commandMap;
    }

    /**
     * 注册多个命令到指定的前缀下。
     *
     * @param fallbackPrefix 命令的备用前缀
     * @param commands       要注册的命令列表
     */
    public void registerAll(@NotNull String fallbackPrefix, @NotNull List<Command> commands) {
        commandMap.registerAll(fallbackPrefix, commands);
    }

    /**
     * 注册单个命令到指定的标签和前缀下。
     *
     * @param label         命令的标签
     * @param fallbackPrefix 命令的备用前缀
     * @param command       要注册的命令
     * @return 如果注册成功则返回 true，否则返回 false
     */
    public boolean register(@NotNull String label, @NotNull String fallbackPrefix, @NotNull Command command) {
        return commandMap.register(label, fallbackPrefix, command);
    }

    /**
     * 注册单个命令到指定的前缀下。
     *
     * @param fallbackPrefix 命令的备用前缀
     * @param command       要注册的命令
     * @return 如果注册成功则返回 true，否则返回 false
     */
    public boolean register(@NotNull String fallbackPrefix, @NotNull Command command) {
        return commandMap.register(fallbackPrefix, command);
    }

    public void unregister(String string) throws NoSuchFieldException, IllegalAccessException {
        // 类型基本检查
        if (knownCommandsField.get(commandMap) instanceof Map<?,?>){
            // 获取实例
            @SuppressWarnings("unchecked")
            Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
            // 移除命令
            knownCommands.remove(string);
        }
    }

    /**
     * 分发命令给指定的发送者。
     *
     * @param sender   命令的发送者
     * @param cmdLine  要执行的命令行
     * @return 如果命令成功执行则返回 true，否则抛出 CommandException
     * @throws CommandException 如果命令执行失败
     */
    public boolean dispatch(@NotNull CommandSender sender, @NotNull String cmdLine) throws CommandException {
        return commandMap.dispatch(sender, cmdLine);
    }

    /**
     * 清除所有已注册的命令。
     */
    public void clearCommands() {
        commandMap.clearCommands();
    }

    /**
     * 根据名称获取已注册的命令。
     *
     * @param name 命令的名称
     * @return 如果找到命令则返回该命令，否则返回 null
     */
    @Nullable
    public Command getCommand(@NotNull String name) {
        return commandMap.getCommand(name);
    }

    /**
     * 为指定的命令行提供自动补全建议。
     *
     * @param sender  命令的发送者
     * @param cmdLine 要补全的命令行
     * @return 补全建议列表，如果无建议则返回 null
     * @throws IllegalArgumentException 如果参数无效
     */
    @Nullable
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String cmdLine) throws IllegalArgumentException {
        return commandMap.tabComplete(sender, cmdLine);
    }

    /**
     * 为指定的命令行和位置提供自动补全建议。
     *
     * @param sender   命令的发送者
     * @param cmdLine  要补全的命令行
     * @param location 发送者的位置（可为空）
     * @return 补全建议列表，如果无建议则返回 null
     * @throws IllegalArgumentException 如果参数无效
     */
    @Nullable
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String cmdLine, @Nullable Location location) throws IllegalArgumentException {
        return commandMap.tabComplete(sender, cmdLine, location);
    }
}
