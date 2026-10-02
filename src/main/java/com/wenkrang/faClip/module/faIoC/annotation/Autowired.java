package com.wenkrang.faClip.module.faIoC.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Autowired {
}
