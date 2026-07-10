package com.wenkrang.faClip.Module.FaCommand.Annotation;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * 命令权限注解
 * 用于标记命令处理方法，指定执行该命令所需的权限节点
 * 
 * 示例：@CmdPermission("plugin.command.use")
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CmdPermission {
    /**
     * 权限节点字符串
     * 
     * @return 权限节点
     */
    @NotNull String value();
}
