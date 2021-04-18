package com.lnjecit.redis.controller;

import com.jay.service.redis.impl.RedisLockImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试地址：
 * http://localhost:8081/distribution/redis/lock1
 */
@RestController
@RequestMapping("/distribution/redis")
public class RedisLockController {
    
    private static final String LOCK_NO = "redis_distribution_lock_no_";
    
    private volatile static int i = 0;
    
    private ExecutorService service;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    /**
     * 模拟1000个线程同时执行业务，修改资源
     * 使用线程池定义了20个线程
     */
    @GetMapping("lock1")
    public void testRedisDistributionLock1() {
        service = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 100; i++) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    task(Thread.currentThread().getName());
                }
            });
        }
        
    }
    
    @GetMapping("/{key}")
    public String getValue(@PathVariable("key") String key) {
        Serializable result = redisTemplate.opsForValue().get(key);
        return result.toString();
    }
    
    private void task(String name) {
//    System.out.println(name + "任务执行中"+(i++)); 
        
        //创建一个redis分布式锁
        RedisLockImpl redisLock = new RedisLockImpl(redisTemplate);
        // 加锁时间
        Long lockTime;
        String lockKey = LOCK_NO + 1;  // 多个线程，使用同一个Key
        if ((lockTime = redisLock.lock(lockKey, name)) != null) {
            //开始执行任务
            startTask(name);
            //任务执行完毕 关闭锁
            redisLock.unlock(lockKey, lockTime, name);
        }
    }
    
    private void startTask(String name) {
        i++;
        System.out.println(name + " 任务执行中" + (i) + " Time:" + formatDate());
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " 任务执行完成: " + (i) + " Time:" + formatDate());
    }
    
    public static String formatDate() {
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss SSS";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
//        System.out.println(sdf.format(date));
        return sdf.format(date);
    }
    
} 
