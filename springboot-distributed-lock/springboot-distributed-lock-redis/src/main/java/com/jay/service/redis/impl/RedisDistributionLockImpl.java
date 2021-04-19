package com.jay.service.redis.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.concurrent.TimeUnit;

/**
 * Created by hetiewei on 2017/4/7.
 */
public class RedisDistributionLockImpl implements RedisDistributionLock {
    
    //加锁超时时间，单位毫秒， 即：加锁时间内执行完操作，如果未完成会有并发现象
    private static final long LOCK_TIMEOUT = 5 * 1000;
    
    private static final Logger LOG = LoggerFactory.getLogger(RedisDistributionLockImpl.class);
    
    private StringRedisTemplate redisTemplate;
    
    public RedisDistributionLockImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * 加锁
     * 取到锁加锁，取不到锁一直等待知道获得锁
     *
     * @param lockKey
     * @param threadName
     * @return
     */
    @Override
    public synchronized long lock(String lockKey, String threadName) {
        LOG.info(threadName + "开始执行加锁");
        while (true) { //循环获取锁
            //锁时间
            Long lock_timeout = currtTimeForRedis() + LOCK_TIMEOUT + 1;
    
            boolean lockFlag =  redisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    //定义序列化方式
                    RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                    byte[] value = serializer.serialize(lock_timeout.toString());
//                    将 key 的值设为 value，当且仅当 key 不存在。
//                    若给定的 key 已经存在，则 SETNX 不做任何动作。
                    boolean flag = redisConnection.setNX(lockKey.getBytes(), value);// 如果已经设置，就返回false
                    return flag;
                }
            });
            if (lockFlag) {
                //如果加锁成功
                LOG.info(threadName + "加锁成功 ++++ 111111");
                
                // 3.锁超时
                //锁超时是什么意思呢？如果一个得到锁的线程在执行任务的过程中挂掉，来不及显式地释放锁，
                // 这块资源将会永远被锁住，别的线程再也别想进来。
                //所以，setnx的key必须设置一个超时时间，单位为second，以保证即使没有被显式释放，
                // 这把锁也要在一定时间后自动释放，避免死锁。setnx不支持超时参数，所以需要额外的指令
                //设置过期时间，释放内存
                redisTemplate.expire(lockKey, LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
                // 如果在没有设置失效之间之前，客户端挂了，就会死锁，这样一来，这把锁就没有设置过期时间，变得“长生不老”，别的线程再也无法获得锁了
                //怎么解决呢？ setNX指令本身是不支持传入超时时间的，幸好Redis 2.6.12以上版本为set指令增加了可选参数，
                //伪代码如下： set（key，1，30，NX）
                //这样就可以取代setnx指令。
                return lock_timeout;// 返回自己加锁的超时时间，后面要根据这个值，判断是不是自己的锁
            } else {
                //获取redis里面的时间
                String result = redisTemplate.opsForValue().get(lockKey);
                Long currt_lock_timeout_str = result == null ? null : Long.parseLong(result);
                //锁已经失效
                if (currt_lock_timeout_str != null && currt_lock_timeout_str < System.currentTimeMillis()) {
                    //判断是否为空，不为空时，说明已经失效，如果被其他线程设置了值，则第二个条件判断无法执行
                    //获取上一个锁到期时间，并设置现在的锁到期时间
                    Long old_lock_timeout_Str = Long.valueOf(redisTemplate.opsForValue().getAndSet(lockKey, lock_timeout.toString()));
                    if (old_lock_timeout_Str != null && old_lock_timeout_Str.equals(currt_lock_timeout_str)) {
                        //多线程运行时，多个线程签好都到了这里，但只有一个线程的设置值和当前值相同，它才有权利获取锁
                        LOG.info(threadName + "加锁成功 ++++ 22222");
                        //设置超时间，释放内存
                        redisTemplate.expire(lockKey, LOCK_TIMEOUT, TimeUnit.MILLISECONDS); // 为锁续航，防止没有执行完业务，锁失效
                        
                        //返回加锁时间
                        return lock_timeout;
                    }else{
                        System.out.println("old_lock_timeout_Str = " + old_lock_timeout_Str + "  currt_lock_timeout_str = result=" + currt_lock_timeout_str );
                    }
                }
            }
            
            try {
                LOG.info(threadName + " 等待加锁, 睡眠100毫秒");
//        TimeUnit.MILLISECONDS.sleep(100);
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 解锁
     *
     * @param lockKey
     * @param lockValue
     * @param threadName
     */
    @Override
    public synchronized void unlock(String lockKey, long lockValue, String threadName) {
        LOG.info(threadName + "完成任务.....执行解锁==========");//正常直接删除 如果异常关闭判断加锁会判断过期时间
        //获取redis中设置的时间
        String result = redisTemplate.opsForValue().get(lockKey);
        Long currt_lock_timeout_str = result == null ? null : Long.valueOf(result);
        
        //如果是加锁者，则删除锁， 如果不是，则等待自动过期，重新竞争加锁
        if (currt_lock_timeout_str != null && currt_lock_timeout_str == lockValue) {// 判断是不是自己的锁
            redisTemplate.delete(lockKey);
            LOG.info(threadName + "解锁成功------------------");
        }
    }
    
    /**
     * 多服务器集群，使用下面的方法，代替System.currentTimeMillis()，获取redis时间，避免多服务的时间不一致问题！！！
     *
     * @return
     */
    @Override
    public long currtTimeForRedis() {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.time();
            }
        });
    }
}
