package com.wenkrang.faClip.Moudle.FaCommand.Annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ParamArrayDes {
     @NotNull String[] value();
}
