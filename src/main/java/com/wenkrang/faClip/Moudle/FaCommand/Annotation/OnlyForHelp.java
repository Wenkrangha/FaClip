package com.wenkrang.faClip.Moudle.FaCommand.Annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface OnlyForHelp {
    boolean value();
}
