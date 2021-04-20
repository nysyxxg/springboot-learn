package com.lnjecit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcConfig {
    
    @Value("com.mysql.cj.jdbc.Driver")
    public String driver;
    
    @Value("${spring.redis.database}")
    public String dbName;
    
    @Value("${spring.redis.host}")
    public String host;
    
    @Value("${spring.redis.port}")
    public String port;
    
    public String getDriver() {
        return driver;
    }
    
    public void setDriver(String driver) {
        this.driver = driver;
    }
    
    public String getDbName() {
        return dbName;
    }
    
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getPort() {
        return port;
    }
    
    public void setPort(String port) {
        this.port = port;
    }
}
