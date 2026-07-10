package com.wenkrang.faClip.Module.FaCommand.AnnotationHandler;

import com.wenkrang.faClip.Module.FaCommand.Annotation.OnlyForHelp;
import com.wenkrang.faClip.Module.FaCommand.FaCmd;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CmdOnlyForHelpHandler implements FaAnnotationHandler{
    @Override
    public void handle(FaCmd command, Method method) {
        command.setOnlyForHelp(true);
    }

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return OnlyForHelp.class;
    }
}
