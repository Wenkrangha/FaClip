package com.wenkrang.faClip.Module.FaCommand.Annotation;

import java.lang.annotation.*;

/**
 * 标记需要 OP 权限的命令
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequireOP {
}
