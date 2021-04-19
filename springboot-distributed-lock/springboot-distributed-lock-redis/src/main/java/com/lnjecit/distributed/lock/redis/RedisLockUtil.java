package com.lnjecit.distributed.lock.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author lnj
 * @description
 * @date 2019-06-29 22:19
 * 可靠性
 * 首先，为了确保分布式锁可用，我们至少要确保锁的实现同时满足以下四个条件：
 * <p>
 * 1:互斥性。在任意时刻，只有一个客户端能持有锁。
 * 2:不会发生死锁。即使有一个客户端在持有锁的期间崩溃而没有主动解锁，也能保证后续其他客户端能加锁。
 * 3:具有容错性。只要大部分的Redis节点正常运行，客户端就可以加锁和解锁。
 * 4: 解铃还须系铃人。加锁和解锁必须是同一个客户端，客户端自己不能把别人加的锁给解了。
 **/
public class RedisLockUtil {
    
    private static final Logger LOG = LoggerFactory.getLogger(RedisLockUtil.class);
    
    private static final String LOCK_SUCCESS = "OK";
    
    private static final Long RELEASE_SUCCESS = 1L;
    
    private static final String SET_IF_NOT_EXIST = "NX";
    
    /**
     * expire time units: EX = seconds; PX = milliseconds
     */
    private static final String SET_WITH_EXPIRE_TIME_UNIT = "PX";
    //加锁超时时间，单位毫秒， 即：加锁时间内执行完操作，如果未完成会有并发现象
    private static final long LOCK_TIMEOUT = 5 * 1000;
    
    /**
     * 尝试获取分布式锁
     *
     * @param jedis      Redis客户端
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 过期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME_UNIT, expireTime);
        return LOCK_SUCCESS.equals(result);
    }
    
    public synchronized static String tryGetLock(Jedis jedis, String lockKey, String requestId,
                                                 String name) {
        LOG.info("线程名称：" + name + "开始执行加锁 " + requestId);
        while (true) { //循环获取锁
            String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME_UNIT, LOCK_TIMEOUT);
            boolean lockFlag = LOCK_SUCCESS.equals(result);
            if (lockFlag) {
                //如果加锁成功
                LOG.info("线程名称：" + name + "加锁成功 ++++ 111111" + requestId);
                return requestId;
            }else{
                System.out.println("线程名称：" + name + "  lockFlag=" + lockFlag + " 获取 result =" + result);
            }
            try {
                LOG.info("线程名称：" + name +  " 等待加锁, 睡眠100毫秒" + requestId);
//        TimeUnit.MILLISECONDS.sleep(100);
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 释放分布式锁
     *
     * @param jedis     Redis客户端
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        return RELEASE_SUCCESS.equals(result);
    }
    
    public synchronized static boolean unlock(Jedis jedis, String lockKey, String requestId, String name) {
        String result = jedis.get(lockKey);
        if (result != null && result.equalsIgnoreCase(requestId)) {
            boolean releaseDistributedLock = releaseDistributedLock(jedis, lockKey, requestId);
            LOG.info("线程名称：" + name + "解锁成功------------------" + requestId);
            return releaseDistributedLock;
        }
        return false;
    }
}
