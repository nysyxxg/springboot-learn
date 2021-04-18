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
        String key = "testDistributedLockKey";
        String requestId = UUID.randomUUID().toString();
        CountDownLatch  countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            System.out.println("--------i--------" + i);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    boolean getDistributedLockResult = RedisLockUtil.tryGetDistributedLock(JEDIS, key, requestId, 60 * 1000 * 60);
                    System.out.println(Thread.currentThread().getName() + " 获取锁Ok.......getDistributedLockResult=" + getDistributedLockResult);
                    System.out.println(Thread.currentThread().getName() + "进入了临界区");
                    if(getDistributedLockResult){
                        try {
                            Thread.sleep(4* 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally {
                            boolean releaseDistributedLockResult = RedisLockUtil.releaseDistributedLock(JEDIS, key, requestId);
                            System.out.println(Thread.currentThread().getName() +  "释放锁......releaseDistributedLockResult=" + releaseDistributedLockResult);
                            countDownLatch.countDown();
                            
                        }
                    }
                   
                }
            };
            thread.start();
        }
    
        countDownLatch.await();
        System.out.println("-------------主线程结束----------------------");
    }
}
