package com.lnjecit.distributed.lock.redis;

import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class RedisLockTest {
    
    private final static Jedis JEDIS;
    
    static {
        JEDIS = new Jedis("127.0.0.1", 6379);
//        JEDIS.auth("123456");
    }
    
    
    @Test
    public void tryGetDistributedLockTest() throws Exception {
        String key = "testDistributedLockKey2";
    
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            System.out.println("--------i--------" + i);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    String requestId = UUID.randomUUID().toString();
                    String name  = Thread.currentThread().getName();
                    String requestIdLock = RedisLockUtil.tryGetLock(JEDIS, key, requestId,name);
                    System.out.println(Thread.currentThread().getName() + " 获取锁Ok.......getDistributedLockResult=" + requestIdLock);
                    System.out.println(Thread.currentThread().getName() + " 进入了临界区");
                    try {
                        Thread.sleep(1 * 1000);
                        System.out.println(Thread.currentThread().getName() + " 任务执行完成!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        boolean releaseDistributedLockResult = RedisLockUtil.unlock(JEDIS, key, requestIdLock,name);
                        System.out.println(Thread.currentThread().getName() + "释放锁......releaseDistributedLockResult=" + releaseDistributedLockResult);
                        countDownLatch.countDown();
                    }
                }
            };
            thread.start();
        }
        
        countDownLatch.await();
        System.out.println("-------------主线程结束----------------------");
    }
    
    @Test
    public void tryDistributedLockTest() throws Exception {
        String key = "testDistributedLockKey1";
        String requestId = UUID.randomUUID().toString();
        
        Thread thread = new Thread() {
            @Override
            public void run() {
                boolean getDistributedLockResult = RedisLockUtil.tryGetDistributedLock(JEDIS, key, requestId, 60 * 1000 * 60);
                System.out.println(Thread.currentThread().getName() + " 获取锁Ok.......getDistributedLockResult=" + getDistributedLockResult);
                System.out.println(Thread.currentThread().getName() + "进入了临界区");
                if (getDistributedLockResult) {
                    try {
                        Thread.sleep(4 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        boolean releaseDistributedLockResult = RedisLockUtil.releaseDistributedLock(JEDIS, key, requestId);
                        System.out.println(Thread.currentThread().getName() + "释放锁......releaseDistributedLockResult=" + releaseDistributedLockResult);
                    }
                }
            }
        };
        thread.start();
        
        System.in.read();
        System.out.println("-------------主线程结束----------------------");
    }
    
    
    @Test
    public void distributedLockTest() throws Exception {
        String key = "testDistributedLockKey1";
        String requestId = UUID.randomUUID().toString();
        
        boolean getDistributedLockResult = RedisLockUtil.tryGetDistributedLock(JEDIS, key, requestId, 60 * 1000 * 60);
        System.out.println(Thread.currentThread().getName() + " 获取锁Ok.......getDistributedLockResult=" + getDistributedLockResult);
        System.out.println(Thread.currentThread().getName() + "进入了临界区");
        if (getDistributedLockResult) {
            try {
                Thread.sleep(4 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                boolean releaseDistributedLockResult = RedisLockUtil.releaseDistributedLock(JEDIS, key, requestId);
                System.out.println(Thread.currentThread().getName() + "释放锁......releaseDistributedLockResult=" + releaseDistributedLockResult);
            }
        }
        
        System.out.println("-------------主线程结束----------------------");
    }
}
