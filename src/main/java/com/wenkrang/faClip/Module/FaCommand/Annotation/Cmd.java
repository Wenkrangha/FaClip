package com.wenkrang.faClip.Module.FaCommand.Annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * 命令节点注解
 * 用于标记命令处理方法，指定其在命令树中的节点路径
 * 
 * 示例：@CmdNode("plugin.command.subcommand")
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Cmd {
    /**
     * 命令节点路径，使用点号分隔
     * 
     * @return 命令节点路径
     */
    @NotNull String value();
}
