package com.chuwa.hw7;

import com.chuwa.hw7.annotation.Autowire;
import com.chuwa.hw7.annotation.Component;
import com.chuwa.hw7.annotation.Scope;
import com.chuwa.hw7.annotation.ScopeType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Minimal IoC container: registers {@link Component} types, resolves {@link Autowire} fields,
 * and honors {@link Scope} ({@link ScopeType#SINGLETON} vs {@link ScopeType#PROTOTYPE}).
 */
public final class ChuwaBeanFactory {

    private final Map<String, BeanDefinition> definitions = new ConcurrentHashMap<>();
    private final Map<String, Object> singletons = new ConcurrentHashMap<>();

    /**
     * Registers component classes (must be annotated with {@link Component}).
     */
    public void register(Class<?>... componentClasses) {
        for (Class<?> c : componentClasses) {
            if (!c.isAnnotationPresent(Component.class)) {
                throw new IllegalArgumentException("Not a @Component: " + c.getName());
            }
            Component comp = c.getAnnotation(Component.class);
            String name = comp.value().isEmpty() ? decapitalize(c.getSimpleName()) : comp.value();
            ScopeType scope = ScopeType.SINGLETON;
            if (c.isAnnotationPresent(Scope.class)) {
                scope = c.getAnnotation(Scope.class).value();
            }
            BeanDefinition def = new BeanDefinition(name, c, scope);
            BeanDefinition prev = definitions.putIfAbsent(name, def);
            if (prev != null) {
                throw new IllegalStateException("Duplicate bean name: " + name);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        List<String> names = new ArrayList<>();
        for (Map.Entry<String, BeanDefinition> e : definitions.entrySet()) {
            if (type.isAssignableFrom(e.getValue().getBeanClass())) {
                names.add(e.getKey());
            }
        }
        if (names.isEmpty()) {
            throw new IllegalArgumentException("No bean assignable to type: " + type.getName());
        }
        if (names.size() > 1) {
            throw new IllegalArgumentException("Multiple beans for type " + type.getName() + ": " + names);
        }
        return (T) getBean(names.get(0));
    }

    public Object getBean(String name) {
        BeanDefinition bd = definitions.get(name);
        if (bd == null) {
            throw new IllegalArgumentException("Unknown bean name: " + name);
        }
        if (bd.getScope() == ScopeType.SINGLETON) {
            return getOrCreateSingleton(bd);
        }
        Object instance = newInstance(bd);
        injectFields(instance);
        return instance;
    }

    private Object getOrCreateSingleton(BeanDefinition bd) {
        String name = bd.getBeanName();
        Object existing = singletons.get(name);
        if (existing != null) {
            return existing;
        }
        synchronized (this) {
            existing = singletons.get(name);
            if (existing != null) {
                return existing;
            }
            Object instance = newInstance(bd);
            singletons.put(name, instance);
            injectFields(instance);
            return instance;
        }
    }

    private static Object newInstance(BeanDefinition bd) {
        try {
            return bd.getBeanClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            throw new IllegalStateException("Cannot instantiate bean: " + bd.getBeanName(), e);
        }
    }

    private void injectFields(Object target) {
        Class<?> clazz = target.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Autowire.class)) {
                continue;
            }
            field.setAccessible(true);
            Object dep = getBean(field.getType());
            try {
                field.set(target, dep);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Cannot inject field: " + clazz.getSimpleName() + "." + field.getName(), e);
            }
        }
    }

    private static String decapitalize(String simpleName) {
        if (simpleName == null || simpleName.isEmpty()) {
            return simpleName;
        }
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}
