package com.lnjecit.springboo.elastic.job.component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MyDataFlowJob implements DataflowJob<String> {
    @Override
    public List<String> fetchData(ShardingContext shardingContext) { //抓取数据
        System.out.println("---------获取数据---------");
        return Arrays.asList("1","2","3");
    }
    @Override
    public void processData(ShardingContext shardingContext, List<String> list) {//处理数据
        System.out.println("---------处理数据---------");
        list.forEach(x-> System.out.println("数据处理:"+x));
    }
}