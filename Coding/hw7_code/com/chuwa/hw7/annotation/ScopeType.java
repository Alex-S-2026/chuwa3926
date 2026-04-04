package com.chuwa.hw7.annotation;

/**
 * Bean lifecycle scope for the mini IoC container.
 */
public enum ScopeType {
    /** One shared instance per container. */
    SINGLETON,
    /** A new instance every time the bean is requested. */
    PROTOTYPE
}
