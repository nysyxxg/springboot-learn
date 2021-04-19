package bat.ke.qq.com.zklock.lock.zk;

import bat.ke.qq.com.zklock.service.OrderCodeGenerator;
import bat.ke.qq.com.zklock.service.OrderService;

import java.util.concurrent.locks.Lock;

/**
 */
public class OrderServiceWithZkLock implements OrderService {

    //用Static修饰来获取用一个订单服务
    private static OrderCodeGenerator generator = new OrderCodeGenerator();

    //新订单创建
    @Override
    public void createOrder() {
        String orderCode = null;
        //分布式锁
        Lock lock = new ZKdistributeLockV2("/studyTest");
        try {
            lock.lock();
            orderCode = generator.getOrderCode();
        } catch (Exception e) {
            lock.unlock();
        }

        System.out.println(Thread.currentThread().getName() + "=============>" + orderCode);
        //业务代码 此处省略100行代码
    }
}