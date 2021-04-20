package com.lnjecit.service.impl;

import com.lnjecit.config.JdbcConfig;
import com.lnjecit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("UserServiceImplV1")
public class UserServiceImpl  implements UserService {
    
    @Autowired
    private JdbcConfig jdbcConfig;
    
    @Override
    public void getJdbcConfig() {
        System.out.println("----------------UserServiceImpl-----------------------");
        System.out.println("driver:" + jdbcConfig.driver);
        System.out.println("dbName:" + jdbcConfig.dbName);
        System.out.println("host:" + jdbcConfig.host);
        System.out.println("port:" + jdbcConfig.port);
    }
    
    @Override
    public void getJdbcConfigFromEnv() {
    
    }
}
