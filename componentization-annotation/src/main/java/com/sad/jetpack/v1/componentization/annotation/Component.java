package com.sad.jetpack.v1.componentization.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

    String[] assetsDir() default {"src\\main\\assets\\"};

    String[] url();

    String description() default "";

    int version() default 1;
}
