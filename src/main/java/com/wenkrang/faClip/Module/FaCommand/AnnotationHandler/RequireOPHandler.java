package com.wenkrang.faClip.Module.FaCommand.AnnotationHandler;

import com.wenkrang.faClip.Module.FaCommand.Annotation.RequireOP;
import com.wenkrang.faClip.Module.FaCommand.FaCmd;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 处理 RequireOP 注解的处理器
 */
public class RequireOPHandler implements FaAnnotationHandler {
    @Override
    public void handle(@NotNull FaCmd command, @NotNull Method method) {
        command.setRequireOP(true);
    }

    @Override
    public @NotNull Class<? extends Annotation> getAnnotationClass() {
        return RequireOP.class;
    }
}
