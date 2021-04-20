package com.lnjecit.service.impl;

import com.lnjecit.config.JdbcConfig;
import com.lnjecit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("UserServiceImplV2")
public class UserServiceImplV2 implements UserService {
    
    @Autowired
    private JdbcConfig jdbcConfig;
    
    @Override
    public void getJdbcConfig() {
        System.out.println("----------------UserServiceImplV2-----------------------");
        System.out.println("driver:" + jdbcConfig.driver);
        System.out.println("dbName:" + jdbcConfig.dbName);
        System.out.println("host:" + jdbcConfig.host);
        System.out.println("port:" + jdbcConfig.port);
    }
}
