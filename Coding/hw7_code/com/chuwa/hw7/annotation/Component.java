package com.chuwa.hw7.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a managed bean in {@link com.chuwa.hw7.ChuwaBeanFactory}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    /**
     * Bean name; if empty, defaults to decapitalized simple class name (e.g. {@code userService}).
     */
    String value() default "";
}
