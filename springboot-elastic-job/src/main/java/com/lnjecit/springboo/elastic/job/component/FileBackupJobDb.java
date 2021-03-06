package com.lnjecit.springboo.elastic.job.component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lnjecit.springboo.elastic.job.bean.FileCustom;
import com.lnjecit.springboo.elastic.job.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 备份文件到数据库中
 */
@Component
public class FileBackupJobDb implements SimpleJob {
    
    private static final Logger logger = LoggerFactory.getLogger(FileBackupJobDb.class);
    
    
    //每次任务执行要备份文件的数量
    private final int FETCH_SIZE = 1;
    
    @Autowired
    FileService fileService;
    
    //任务执行代码逻辑
    @Override
    public void execute(ShardingContext shardingContext) {
        logger.info("作业分片：" + shardingContext.getShardingItem());
        //分片参数，（0=text,1=image,2=radio,3=vedio，参数就是text、image...）
        String jobParameter = shardingContext.getShardingParameter();
        //获取未备份的文件
        List<FileCustom> fileCustoms = fetchUnBackupFiles(jobParameter, FETCH_SIZE);
        //进行文件备份
        backupFiles(fileCustoms);
    }
    
    /**
     * 获取未备份的文件
     *
     * @param count 文件数量
     * @return
     */
    public List<FileCustom> fetchUnBackupFiles(String fileType, int count) {
        
        List<FileCustom> fileCustoms = fileService.fetchUnBackupFiles(fileType, count);
        logger.info("time:%s,获取文件%d个\n", LocalDateTime.now(), count);
        return fileCustoms;
        
    }
    
    /**
     * 文件备份
     *
     * @param files
     */
    public void backupFiles(List<FileCustom> files) {
        fileService.backupFiles(files);
    }
}
