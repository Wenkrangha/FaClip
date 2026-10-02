package com.wenkrang.faClip.module.faIoC.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Service {
    Scope scope() default Scope.SINGLETON;
    public enum Scope { SINGLETON, PROTOTYPE }
}

