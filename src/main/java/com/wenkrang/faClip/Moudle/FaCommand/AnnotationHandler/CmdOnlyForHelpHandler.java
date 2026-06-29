package com.wenkrang.faClip.Moudle.FaCommand.AnnotationHandler;

import com.wenkrang.faClip.Moudle.FaCommand.Annotation.Help;
import com.wenkrang.faClip.Moudle.FaCommand.Annotation.OnlyForHelp;
import com.wenkrang.faClip.Moudle.FaCommand.FaCmd;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CmdOnlyForHelpHandler implements FaAnnotationHandler{
    @Override
    public void handle(FaCmd command, Method method) {
        boolean value = method.getAnnotation(OnlyForHelp.class).value();

        command.setOnlyForHelp(value);
    }

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return OnlyForHelp.class;
    }
}
