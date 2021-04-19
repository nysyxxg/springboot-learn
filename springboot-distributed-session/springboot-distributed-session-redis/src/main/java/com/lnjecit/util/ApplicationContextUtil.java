package com.lnjecit.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

//在自定义的类中获取工厂中已经创建好的某些对象
@Configuration
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    //根据bean id获取对象
    public static Object getBean(String id){
        return context.getBean(id);
    }

    //根据bean 类型获取对象
    public static Object getBean(Class clazz){
        return context.getBean(clazz);
    }

    //根据bean id和类型获取对象
    public static Object getBean(String id,Class clazz){
        return context.getBean(id,clazz);
    }

}
