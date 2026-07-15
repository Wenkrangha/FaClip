package com.wenkrang.faClip.Module.FaCommand.AnnotationHandler;

import com.wenkrang.faClip.Module.FaCommand.Annotation.ForPlayer;
import com.wenkrang.faClip.Module.FaCommand.FaCmd;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CmdPlayerHandler implements FaAnnotationHandler{
    @Override
    public void handle(FaCmd command, Method method) {
        command.setForPlayer(true);
    }

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return ForPlayer.class;
    }
}
