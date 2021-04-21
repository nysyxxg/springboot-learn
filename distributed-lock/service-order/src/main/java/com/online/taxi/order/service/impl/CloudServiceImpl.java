package com.online.taxi.order.service.impl;

import com.online.taxi.order.annotation.DistributedLock;
import com.online.taxi.order.annotation.DistributedLock;
import com.online.taxi.order.service.GrabService;
import com.online.taxi.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author yueyi2019
 */
@Service("cloudService")
public class CloudServiceImpl implements GrabService {

    @Autowired
    OrderService orderService;

    @Override
    @DistributedLock(value = "redisLockRegistry",time = 10)
    public String grabOrder(int orderId, int driverId) {

        System.out.println("司机:" + driverId + " 执行抢单逻辑");
        try {
            TimeUnit.MINUTES.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean b = orderService.grab(orderId, driverId);
        if (b) {
            System.out.println("司机:" + driverId + " 抢单成功");
        } else {
            System.out.println("司机:" + driverId + " 抢单失败");
        }

        return null;
    }
}