package com.lnjecit.springboo.elastic.job.config;

import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.lnjecit.springboo.elastic.job.util.ElasticJobUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class MyScriptJobConf {
//    @Autowired
//    CoordinatorRegistryCenter regCenter;
    
    private static String serverLists = "localhost:2181";
    private static String namespace = "data-archive-job-1";
    
    /**
     * 配置任务调度: 参数:  任务
     * zk注册中心
     * 任务详情
     */
    @Bean(initMethod = "init")
    public JobScheduler scriptJobScheduler(@Value("${myScriptJob.cron}") final String cron,  //yml注入
                                           @Value("${myScriptJob.shardingTotalCount}") final int shardingTotalCount,
                                           @Value("${myScriptJob.shardingItemParameters}") final String shardingItemParameters) {
        
        CoordinatorRegistryCenter regCenter = setUpRegistryCenter();
        String scriptCommandLine = "echo hello";
        return new SpringJobScheduler(null, regCenter,
                ElasticJobUtils.getScriptJobConfiguration(
                        "script_job",
                        cron,
                        shardingTotalCount,
                        //命令或者脚本路径
                        shardingItemParameters, scriptCommandLine)
                //,new MyElasticJobListener() 可配置监听器
        );
    }
    
    
    private CoordinatorRegistryCenter setUpRegistryCenter() {
        //        // 配置分布式协调服务（注册中心）Zookeeper
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(serverLists, namespace);
        //减少zk的超时时间
        zookeeperConfiguration.setSessionTimeoutMilliseconds(100);
        // 创建注册中心
        CoordinatorRegistryCenter coordinatorRegistryCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
        // 初始化
        coordinatorRegistryCenter.init();
        return coordinatorRegistryCenter;
    }
}