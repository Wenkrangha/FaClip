package com.wenkrang.faClip.Moudle.FaCommand.AnnotationHandler;

import com.wenkrang.faClip.Moudle.FaCommand.Annotation.Cmd;
import com.wenkrang.faClip.Moudle.FaCommand.FaCmd;
import com.wenkrang.faClip.Moudle.FaCommand.Helper.CmdNodeHelper;
import com.wenkrang.faClip.Moudle.FaMessage.Fm;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static com.wenkrang.faClip.Moudle.FaMessage.Helper.i18nHelper.ft;
import static com.wenkrang.faClip.Moudle.FaMessage.Helper.i18nHelper.t;

/**
 * CmdNode 注解处理器
 * 负责处理 @CmdNode 注解，提取命令节点路径并设置到 FaCmd 对象中
 */
public class CmdNodeHandler implements FaAnnotationHandler {
    @Override
    public void handle(@NotNull FaCmd command, @NotNull Method method) {
        Cmd cmd = method.getAnnotation(Cmd.class); // 获取命令节点

        if (!Modifier.isStatic(method.getModifiers()))
            throw new RuntimeException(ft("FaCommand.Error.Interpreter.NotStatic", method.getName()));

        String node = cmd.value(); // 获取挂载的命令节点

        // 检查命令节点是否合规
        if (CmdNodeHelper.check(node)) {
            //进行泛命令加载
            command.setNode(node);
            command.setMethod(method);
        }else {
            Fm.waring(t("FaCommand.Error.Interpreter.CantUnderstand"));
        }
    }


    @Override
    public @NotNull Class<? extends Annotation> getAnnotationClass() {
        return Cmd.class;
    }


}
