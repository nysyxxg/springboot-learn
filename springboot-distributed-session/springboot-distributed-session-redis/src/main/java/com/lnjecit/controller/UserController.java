package com.lnjecit.controller;

import com.lnjecit.config.JdbcConfig;
import com.lnjecit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 测试URL：
 * http://localhost:8091/testredis1/user/getSessionId
 *  http://localhost:8091/testredis1/user/getJdbcConfig
 * @author lnj
 * @description UserController
 * @date 2019-01-12 15:43
 **/
@RequestMapping("user")
@RestController
public class UserController {

    @Autowired
    private JdbcConfig jdbcConfig;
    
    @Autowired
    @Qualifier("UserServiceImplV2") //使用 UserServiceImplV2 这个实现类
    private UserService userService;
    
    @GetMapping("getSessionId")
    public String getSessionId(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        System.out.println(sessionId);
    
        System.out.println("driver:" + jdbcConfig.driver);
        System.out.println("dbName:" + jdbcConfig.dbName);
        System.out.println("host:" + jdbcConfig.host);
        System.out.println("port:" + jdbcConfig.port);
        return sessionId;
    }
    
    
    @GetMapping("getJdbcConfig")
    public String getJdbcConfig(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        System.out.println(sessionId);
    
        userService.getJdbcConfig();
        
        return sessionId;
    }
    
}
