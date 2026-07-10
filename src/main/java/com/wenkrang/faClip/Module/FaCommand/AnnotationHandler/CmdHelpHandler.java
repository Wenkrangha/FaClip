package com.wenkrang.faClip.Module.FaCommand.AnnotationHandler;

import com.wenkrang.faClip.Module.FaCommand.Annotation.Help;
import com.wenkrang.faClip.Module.FaCommand.FaCmd;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CmdHelpHandler implements FaAnnotationHandler{
    @Override
    public void handle(FaCmd command, Method method) {
        String value = method.getAnnotation(Help.class).value();

        command.setHelp(value);
    }

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return Help.class;
    }
}
