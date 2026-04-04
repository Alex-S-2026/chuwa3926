package com.chuwa.hw7;

import com.chuwa.hw7.annotation.ScopeType;

/**
 * Metadata for one registered bean.
 */
final class BeanDefinition {
    private final String beanName;
    private final Class<?> beanClass;
    private final ScopeType scope;

    BeanDefinition(String beanName, Class<?> beanClass, ScopeType scope) {
        this.beanName = beanName;
        this.beanClass = beanClass;
        this.scope = scope;
    }

    String getBeanName() {
        return beanName;
    }

    Class<?> getBeanClass() {
        return beanClass;
    }

    ScopeType getScope() {
        return scope;
    }
}
