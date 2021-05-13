package com.lnjecit.springboo.elastic.job.config;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticJobRegistryCenterConfig {
    
    //zookeeper链接字符串 localhost:2181
    private String ZOOKEEPER_CONNECTION_STRING = "192.168.180.113:2181";
    //定时任务命名空间
    private String JOB_NAMESPACE = "elastic-job-boot-java";
    
    //zk的配置及创建注册中心
    @Bean(initMethod = "init")
    public CoordinatorRegistryCenter setUpRegistryCenter() {
        //zk的配置
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(ZOOKEEPER_CONNECTION_STRING, JOB_NAMESPACE);
        
        zookeeperConfiguration.setSessionTimeoutMilliseconds(1000);
        //创建注册中心
        CoordinatorRegistryCenter zookeeperRegistryCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
        return zookeeperRegistryCenter;
        
    }
}
