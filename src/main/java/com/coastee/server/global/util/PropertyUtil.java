package com.coastee.server.global.util;

import org.springframework.core.env.Environment;

public class PropertyUtil {

    private static Environment environment(){
        return BeanContext.get(Environment.class);
    }

    public static String getProperty(String key){
        return environment().getProperty(key);
    }
}

