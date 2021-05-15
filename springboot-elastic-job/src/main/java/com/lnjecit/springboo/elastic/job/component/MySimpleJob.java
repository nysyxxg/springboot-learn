package com.lnjecit.springboo.elastic.job.component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MySimpleJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println(shardingContext.getJobName()+"执行:"+
                "分片参数:"+shardingContext.getShardingParameter()+
                ",当前分片项:"+shardingContext.getShardingItem()+
                ",time:"+ LocalDate.now());
    }
}