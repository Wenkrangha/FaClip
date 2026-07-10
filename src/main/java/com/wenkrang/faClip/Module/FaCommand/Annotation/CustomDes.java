package com.wenkrang.faClip.Module.FaCommand.Annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CustomDes {
     @NotNull Class<? extends DesProvider> value();
}
