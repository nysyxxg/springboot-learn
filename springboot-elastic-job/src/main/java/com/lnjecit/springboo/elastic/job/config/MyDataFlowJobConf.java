package com.lnjecit.springboo.elastic.job.config;

import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.lnjecit.springboo.elastic.job.component.MyDataFlowJob;
import com.lnjecit.springboo.elastic.job.util.ElasticJobUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class MyDataFlowJobConf {
    @Autowired
    CoordinatorRegistryCenter regCenter;
    
    @Autowired
    MyDataFlowJob myDataFlowJob;
    /**
     * 配置任务调度: 参数:  任务
     *                    zk注册中心
     *                    任务详情
     */
    @Bean(initMethod = "init")
    public JobScheduler dataFlowJobScheduler(@Value("${myDataFlowJob.cron}") final String cron,  //yml注入
                                             @Value("${myDataFlowJob.shardingTotalCount}") final int shardingTotalCount,
                                             @Value("${myDataFlowJob.shardingItemParameters}") final String shardingItemParameters) {
        return new SpringJobScheduler(myDataFlowJob, regCenter,
                ElasticJobUtils.getDataFlowJobConfiguration(
                        myDataFlowJob.getClass(),
                        cron,
                        shardingTotalCount,
                        shardingItemParameters,true)
                //,new MyElasticJobListener() 可配置监听器
        );
    }
}