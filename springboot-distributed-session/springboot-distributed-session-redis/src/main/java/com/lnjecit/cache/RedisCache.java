package com.lnjecit.cache;

import com.lnjecit.util.ApplicationContextUtil;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 重写 Mybatis cache实现redis缓存
 * 不能交给工厂管理
 * */
public class RedisCache implements Cache {
    //必须定义这个String类型的id,因为这个id表示当前加入缓存的namespace;
     private String id;

    public RedisCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    //放入缓存
    @Override
    public void putObject(Object key, Object value) {
        //获取redisTemplate对象
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtil.getBean("redisTemplate");
        //存储缓存数据
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置HashKey序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //hash模型
        redisTemplate.opsForHash().put(id,key.toString(),value);
    }

    //从缓存中获取
    @Override
    public Object getObject(Object key) {
        //获取redisTemplate对象
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtil.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置HashKey序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate.opsForHash().get(id.toString(),key.toString());
    }

    //从缓存中移除
    //真正使用过程中，这个方法并不会被用到
    @Override
    public Boolean removeObject(Object key) {
        return null;
    }

    //清除缓存
    @Override
    public void clear() {
        //获取redisTemplate对象
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtil.getBean("redisTemplate");
        redisTemplate.delete(id);
    }

    //缓存命中率计算
    @Override
    public int getSize() {
//      获取redisTemplate对象
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtil.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate.opsForHash().size(id.toString()).intValue();
    }

    /*
    * ReadWriteLock读写锁 表示：读写之间互斥，读读之间不互斥，写写之间不互斥
    * 区别于Synchronized  表示：读读之间互斥，写写之阿互斥，读写之间互斥
    * 因此ReadWriteLock效率比Synchronized高
    * 对于缓存，只有写操作，没有写操作
    * */
    @Override
    public ReadWriteLock getReadWriteLock() {
        return new ReentrantReadWriteLock();
    }
}
