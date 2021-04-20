package com.lnjecit.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RunEnvironment {
    private static Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        RunEnvironment.environment = environment;
    }

    public static String getProperty(String key){
        return environment.getProperty(key);
    }
}