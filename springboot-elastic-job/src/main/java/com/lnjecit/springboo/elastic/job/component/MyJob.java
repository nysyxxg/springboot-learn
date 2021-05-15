package com.lnjecit.springboo.elastic.job.component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
@Component
public class MyJob implements SimpleJob {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println(dateFormat.format(new Date()) + ":定时任务执行逻辑");
    }
}
