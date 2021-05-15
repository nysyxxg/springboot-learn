package com.lnjecit.springboo.elastic.job.service;

import com.lnjecit.springboo.elastic.job.bean.FileCustom;

import java.util.List;

public interface FileService {
    
    public List<FileCustom> fetchUnBackupFiles(String fileType, Integer count);
    public void backupFiles(List<FileCustom> files);
    
    public void println();
    
}
