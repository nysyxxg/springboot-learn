package bat.ke.qq.com.zklock.controller;

import bat.ke.qq.com.zklock.demo.SimpleDistributedLockMutex;
import bat.ke.qq.com.zklock.lock.Lock;
import bat.ke.qq.com.zklock.lock.zk.ZKdistributeLockV2;
import bat.ke.qq.com.zklock.lock.zk.ZkDistributedLock;
import bat.ke.qq.com.zklock.service.OrderCodeGenerator;
import org.I0Itec.zkclient.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 测试地址：
 * http://localhost:8081/getOrderCode3
 */
@RestController
public class OrderController {
    
    @Autowired
    private OrderCodeGenerator orderCodeGenerator;
    
    @Autowired
    private Lock zkLock;
    
    @Autowired
    private InterProcessMutex cunratorLock;
    
    @Autowired
    private SimpleDistributedLockMutex simpleDistributedLockMutex;
    
    private ExecutorService service;
    
    
    @RequestMapping("/getOrderCode")
    public void getOrderCode() {
        try {
            zkLock.lock();
            String orderCode = orderCodeGenerator.getOrderCode();
            System.out.println("生产订单号：" + orderCode);
        } finally {
            zkLock.unlock();
        }
    }
    
    @RequestMapping("/getOrderCode1")
    public void getOrderCode1() {
        service = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 100; i++) { // 模拟100个线程
            service.execute(new Runnable() {
                @Override
                public void run() {
                    task(Thread.currentThread().getName());
                }
            });
        }
        
    }
    
    private void task(String name) {
        try {
            zkLock.lock();
            String orderCode = orderCodeGenerator.getOrderCode();
            System.out.println("线程名称: " + name + "  生产订单号：" + orderCode);
        } finally {
            zkLock.unlock();
        }
    }
    
    
    @Bean
    public Lock zkLock() {
        //基于临时节点
        String config = "127.0.0.1:2181";
        String path = "/lock";
//        return new ZkDistributedLock(path, config);
        return new ZKdistributeLockV2(path, config);
    }
    
    /**
     * 时序锁
     * cunratorLock
     * 底层实现原理 https://www.processon.com/diagraming/5f39ec1ae401fd0be0316f57
     */
    @RequestMapping("/getOrderCode2")
    public void getOrderCode2() {
        try {
            //加锁
            cunratorLock.acquire();
            String orderCode = orderCodeGenerator.getOrderCode();
            System.out.println("生成订单号：" + orderCode);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //释放锁
                cunratorLock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Curator是ZooKeeper的一个客户端框架，其中封装了分布式互斥锁的实现，最为常用的是InterProcessMutex
     * InterProcessMutex基于Zookeeper实现了分布式的公平可重入互斥锁
     * InterProcessMutex的特性
     * 分布式锁（基于Zookeeper）
     * 互斥锁
     * 公平锁（监听上一临时顺序节点 + wait() / notifyAll()）
     * 可重入
     *
     * @return
     */
    @Bean
    public InterProcessMutex cunratorLock() {
        //基于临时有序节点
        String config = "127.0.0.1:2181";
        String path = "/locks";
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(config)
                .retryPolicy(new ExponentialBackoffRetry(100, 1)).build();
        //retryPolicy 重试策略
        client.start();
        return new InterProcessMutex(client, path);
    }
    
    
    @Bean
    public SimpleDistributedLockMutex getZkLock() {
        //基于临时节点
        String config = "127.0.0.1:2181";
        String basePath = "/lock";
        ZkClient client = new ZkClient(config);
        return new SimpleDistributedLockMutex(client, basePath);
    }
    
    
    @RequestMapping("/getOrderCode3")
    public void getOrderCode3() {
        service = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 100; i++) { // 模拟100个线程
            service.execute(new Runnable() {
                @Override
                public void run() {
                    runOrderTask(Thread.currentThread().getName());
                }
            });
        }
        
    }
    
    private void runOrderTask(String name) {
        try {
            // 不带超时
            simpleDistributedLockMutex.acquire();
//            simpleDistributedLockMutex.acquire(2, TimeUnit.SECONDS);//如果设置时间，造成锁超时，就会抛出异常，不会只想后面的订单生产代码
            String orderCode = orderCodeGenerator.getOrderCode();
            System.out.println("线程名称: " + name + "  生产订单号：" + orderCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                simpleDistributedLockMutex.release();
                System.out.println("线程名称: " + name + "  释放锁........." );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
}
