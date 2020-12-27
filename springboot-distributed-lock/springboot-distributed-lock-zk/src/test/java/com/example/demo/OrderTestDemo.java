package com.example.demo;

import bat.ke.qq.com.zklock.lock.jdk.OrderServiceWithLock;
import bat.ke.qq.com.zklock.lock.zk.OrderServiceWithZkLock;
import bat.ke.qq.com.zklock.service.OrderService;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 */
public class OrderTestDemo {
    
    public static void main(String[] args) {
        //设置并发数
        int currentThread = 30;
        System.out.println("用户下单开始============");
        //循环屏障
        CyclicBarrier cyclicBarrier = new CyclicBarrier(currentThread);

//        OrderService orderService = new OrderServiceImpl();

//        OrderService orderService = new OrderServiceWithLock();
        
        //多线程模拟并发
        for (int i = 0; i <= currentThread; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "========》用户下单");
                //订单服务移进来 可以模拟多个 TOMCAT
                //OrderService orderService = new OrderServiceWithLock(); //将对象放在这里和放在外面是不一样的
    
                OrderService orderService = new OrderServiceWithZkLock();
                try {
                    //等待一起执行
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                orderService.createOrder();
            }).start();
        }
        
    }
}