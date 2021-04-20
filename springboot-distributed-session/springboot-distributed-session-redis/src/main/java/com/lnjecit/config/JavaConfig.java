package com.lnjecit.config;

import com.lnjecit.service.UserService;
import com.lnjecit.service.UserServiceV1;
import com.lnjecit.service.impl.UserServiceImplV1;
import com.lnjecit.service.impl.UserServiceImplV2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaConfig {
    
    @Bean
    public MyBean functionService() {
        return new MyBean();
    }
}