package com.lnjecit.redis.cfg;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
@Configuration
public class RedissonConfig {
 
    @Bean
    public RLock redissonLock(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0);
        RedissonClient client = Redisson.create(config);
        return  client.getLock("redissonLock_name");
    }
}