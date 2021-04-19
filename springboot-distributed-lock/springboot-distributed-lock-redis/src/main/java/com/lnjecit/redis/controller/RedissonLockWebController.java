package com.lnjecit.redis.controller;

import com.lnjecit.redis.bean.Stock;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 启动后 访问测试地址 http://127.0.0.1:8081/startReduce
 */
@RestController
public class RedissonLockWebController {
    
    @Autowired
    RLock redissonLock;
    
    @GetMapping("/startReduce")
    public String startReduce() {
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                redissonLock.lock(15, TimeUnit.SECONDS);
                boolean b = Stock.reduceStock();// 业务处理逻辑是20S, 但是锁的有效期只有 15S ，
                // 所以，下面去释放锁的时候，就会抛出异常
                System.out.println(Thread.currentThread().getName() + "下单:" + (b ? "成功" : "失败"));
                redissonLock.unlock();
            }
        }).start();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                redissonLock.lock(15, TimeUnit.SECONDS);
                boolean b = Stock.reduceStock();
                System.out.println(Thread.currentThread().getName() + "下单:" + (b ? "成功" : "失败"));
                redissonLock.unlock();
            }
        }).start();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                redissonLock.lock(15, TimeUnit.SECONDS);
                boolean b = Stock.reduceStock();
                System.out.println(Thread.currentThread().getName() + "下单:" + (b ? "成功" : "失败"));
                redissonLock.unlock();
            }
        }).start();
        
        try {
            Thread.sleep(61* 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Stock.count = " + Stock.count);
        return "set ok!";
    }
    
}