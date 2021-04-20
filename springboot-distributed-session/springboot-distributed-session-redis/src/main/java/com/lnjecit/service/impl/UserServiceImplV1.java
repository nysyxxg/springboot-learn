package com.lnjecit.service.impl;

import com.lnjecit.component.RunEnvironment;
import com.lnjecit.config.JdbcConfig;
import com.lnjecit.service.UserService;
import com.lnjecit.service.UserServiceV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("UserServiceImplV1")
public class UserServiceImplV1 implements UserServiceV1 {
    
    @Autowired
    private JdbcConfig jdbcConfig;
    
    @Qualifier
    private RunEnvironment runEnvironment;
    
    @Override
    public void getJdbcConfig() {
        System.out.println("----------------UserServiceImplV2-----------getJdbcConfig------------");
        System.out.println("driver:" + jdbcConfig.getDriver());
        System.out.println("dbName:" + jdbcConfig.getDbName());
        System.out.println("host:" + jdbcConfig.host);
        System.out.println("port:" + jdbcConfig.port);
    }
    
    @Override
    public void getJdbcConfigFromEnv() {
        System.out.println("----------------UserServiceImplV2-----getJdbcConfigFromEnv------------------");
        System.out.println("driver:" + runEnvironment.getProperty(""));
        System.out.println("dbName:" + runEnvironment.getProperty("spring.redis.database"));
        System.out.println("host:" + runEnvironment.getProperty("spring.redis.host"));
        System.out.println("port:" + runEnvironment.getProperty("spring.redis.port"));
        
        
    }
}
