package com.wenkrang.faClip.Moudle.FaCommand.Helper;

import com.wenkrang.faClip.Moudle.FaCommand.FaCmd;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CmdHandleHelper {

    /**
     * 检查指定命令是否未注册
     *
     * @param s 要检查的命令名称
     * @return 如果命令未注册则返回true，否则返回false
     */
    public static boolean isUnregistered(@NotNull String s) {
        return Bukkit.getPluginCommand(s) == null;
    }

    /**
     * 处理根命令的注册逻辑
     *
     * @param rootCommand 根命令名称
     * @param command FaCmd命令对象
     */
    public static void handleRootCommand(@NotNull String rootCommand, @NotNull FaCmd command) {
        // 检查根命令是否注册
        if (isUnregistered(rootCommand)) {
            // 创建新的命令实例并设置执行逻辑
            Command newCommand = new Command(rootCommand) {
                @Override
                public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                    //调用解释器处理
                    return command.getInterpreter().interpret(sender, commandLabel, args);
                }

                @Override
                public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                    //调用解释器处理
                    return command.getInterpreter().tabComplete(sender, alias, args);
                }
            };



            // 注册新命令到命令映射中
            command.getCommandMap().register(rootCommand, newCommand);

            // 设置命令对象的内部命令引用
            command.setCommand(newCommand);


        } else {
            // 如果命令已存在，则直接获取现有命令
            command.setCommand(Bukkit.getPluginCommand(rootCommand));
        }
    }


}
