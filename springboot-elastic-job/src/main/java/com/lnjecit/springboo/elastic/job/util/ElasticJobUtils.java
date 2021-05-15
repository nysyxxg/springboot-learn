package com.lnjecit.springboo.elastic.job.util;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;

public class ElasticJobUtils {

    /**
     * 创建简单任务详细信息
     */
    public static LiteJobConfiguration getSimpleJobConfiguration(final Class<? extends SimpleJob> jobClass, //任务类
                                                                 final String cron,    // 运行周期配置
                                                                 final int shardingTotalCount,  //分片个数
                                                                 final String shardingItemParameters) {  // 分片参数
        return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(
                JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount)
                        .shardingItemParameters(shardingItemParameters).build()
                , jobClass.getCanonicalName())
        ).overwrite(true).build();
    }

    /**
     * 创建流式作业配置
     */
    public static LiteJobConfiguration getDataFlowJobConfiguration(final Class<? extends DataflowJob> jobClass, //任务类
                                                                   final String cron,    // 运行周期配置
                                                                   final int shardingTotalCount,  //分片个数
                                                                   final String shardingItemParameters,
                                                                   final Boolean streamingProcess   //是否是流式作业
                                                                   ) {  // 分片参数
        return LiteJobConfiguration.newBuilder(new DataflowJobConfiguration(
                JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount)
                        .shardingItemParameters(shardingItemParameters).build()
                // true为流式作业,除非fetchData返回数据为null或者size为0,否则会一直执行
                // false 非流式,只会按配置时间执行一次
                , jobClass.getCanonicalName(),streamingProcess)
        ).overwrite(true).build();
    }
    
    /**
     * 创建脚本作业配置
     */
    public static LiteJobConfiguration getScriptJobConfiguration(final String jobName, //任务名字
                                                                 final String cron,    // 运行周期配置
                                                                 final int shardingTotalCount,  //分片个数
                                                                 final String shardingItemParameters,
                                                                 final String scriptCommandLine   //是脚本路径或者命令
    ) {  // 分片参数
        return LiteJobConfiguration.newBuilder(new ScriptJobConfiguration(
                JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount)
                        .shardingItemParameters(shardingItemParameters).build()
                // 此处配置文件路径或者执行命令
                , scriptCommandLine)
        ).overwrite(true).build();
    }
    
}