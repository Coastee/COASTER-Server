package com.coastee.server.global.util;

import org.springframework.context.ApplicationContext;

public class BeanContext {
    private static ApplicationContext context;

    public static void init(final ApplicationContext context) {
        BeanContext.context = context;
    }

    public static <T> T get(final Class<T> clazz) {
        return context.getBean(clazz);
    }
}
