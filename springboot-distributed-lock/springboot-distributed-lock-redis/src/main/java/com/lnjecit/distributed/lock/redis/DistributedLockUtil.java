package com.lnjecit.distributed.lock.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
 

/**
 * 分布式锁工具<br/>
 * 说明:基于redis，使用setNx命令。使用value为时间戳的形式，保证不会一直持有锁
 */
@Component
public class DistributedLockUtil {
 
    private static RedisTemplate<Object, Object> redisTemplate;
 
    public DistributedLockUtil(RedisTemplate<Object, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    /**
     * 加锁默认超时时间
     */
    private static final long DEFAULT_TIMEOUT_SECOND = 5;
 
    /**
     * 加锁循环等待时间
     */
    private static final long LOOP_WAIT_TIME_MILLISECOND = 30;
 
    /**
     * 加锁
     * @param key
     * @param timeoutSecond 如果为null,使用默认超时时间
     * @return 加锁的值（超时时间）
     */
    public static long lock(String key, Long timeoutSecond){
    
        System.out.println("Thread：" + Thread.currentThread().getName() + " start lock");
 
        //如果参数错误
        if(timeoutSecond != null && timeoutSecond <= 0){
            timeoutSecond = DEFAULT_TIMEOUT_SECOND;
        }
        timeoutSecond = timeoutSecond == null? DEFAULT_TIMEOUT_SECOND : timeoutSecond;
 
        while (true){
            //超时时间点
            long timeoutTimeMilli = currentTimeMilliForRedis() + timeoutSecond * 1000;
            //如果设置成功
            if(redisTemplate.opsForValue().setIfAbsent(key, timeoutTimeMilli)){
                System.out.println("Thread：" + Thread.currentThread().getName() + " lock success");
                return timeoutTimeMilli;
            }
 
            //如果已经超时
            Long value = (Long)redisTemplate.opsForValue().get(key);
            if(value != null && value.longValue() < currentTimeMilliForRedis()) {
                //设置新的超时时间
                Long oldValue = (Long) redisTemplate.opsForValue().getAndSet(key, timeoutTimeMilli);//旧的值
                //多个线程同时getset，只有第一个才可以获取到锁
                if (value.equals(oldValue)) {
                    System.out.println("Thread：" + Thread.currentThread().getName() + " lock success");
                    return timeoutTimeMilli;
                }
            }
 
            //延迟一定毫秒，防止请求太频繁
            try {
                Thread.sleep(LOOP_WAIT_TIME_MILLISECOND);
            } catch (InterruptedException e) {
                System.out.println("DistributedLockUtil lock sleep error");
                e.printStackTrace();
            }
        }
    }
 
    /**
     * 释放锁
     * @param key
     * @param lockValue
     */
    public static void unLock(String key, long lockValue){
        System.out.println("Thread：" + Thread.currentThread().getName() + " start unlock");
        Long value = (Long)redisTemplate.opsForValue().get(key);
        if(value != null && value.equals(lockValue)) {//如果是本线程加锁
            redisTemplate.delete(key);
            System.out.println("Thread：" + Thread.currentThread().getName() + " unlock success");
        }
    }
 
    /**
     * redis服务器时间
     * @return
     */
    private static long currentTimeMilliForRedis(){
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.time();
            }
        });
    }
 
}