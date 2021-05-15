package com.lnjecit.springboo.elastic.job;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.lnjecit.springboo.elastic.job.component.MyJob;

/**
 * 启动任务
 */
public class JobMainDriver1 {
    private static String serverLists = "localhost:2181";
    private static String namespace = "data-archive-job-1";
    
    public static void main(String[] args) {
        CoordinatorRegistryCenter coordinatorRegistryCenter = setUpRegistryCenter();
        
        // 配置任务（时间事件、定时任务业务逻辑、调度器）
        startJob(coordinatorRegistryCenter);
    }
    
    private static CoordinatorRegistryCenter setUpRegistryCenter() {
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
    
    // 任务配置和启动任务
    private static void startJob(CoordinatorRegistryCenter coordinatorRegistryCenter) {
        String jobName = "archive-job-1";
        String cron = "*/2 * * * * ?";
        int shardingTotalCount = 1;
        String jobClass = MyJob.class.getName();
//        String jobClass = FileBackupJob.class.getName();
        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount).build();
        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, jobClass);
        
        JobScheduler jobScheduler = new JobScheduler(coordinatorRegistryCenter, LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true).build());
        jobScheduler.init();
    }
}
