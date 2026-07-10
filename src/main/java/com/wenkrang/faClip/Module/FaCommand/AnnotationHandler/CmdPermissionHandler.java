package com.wenkrang.faClip.Module.FaCommand.AnnotationHandler;

import com.wenkrang.faClip.Module.FaCommand.Annotation.CmdPermission;
import com.wenkrang.faClip.Module.FaCommand.FaCmd;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * CmdPermission 注解处理器
 * 负责处理 @CmdPermission 注解，提取权限节点并设置到 FaCmd 对象中
 */
public class CmdPermissionHandler implements FaAnnotationHandler{
    @Override
    public void handle(FaCmd command, Method method) {
        CmdPermission cmdPermission = method.getAnnotation(CmdPermission.class);
        command.setPermission(cmdPermission.value());
    }

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return CmdPermission.class;
    }
}
