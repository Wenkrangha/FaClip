package com.wenkrang.faClip.module.faInterface.handler;

import com.wenkrang.faClip.module.faInterface.FaIntf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface SimpleAnnotationHandler {
    void handle(FaIntf faIntf, Method method);
    Class<? extends Annotation> getAnnotationClass();
}
