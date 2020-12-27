package bat.ke.qq.com.zklock.lock.jdk;

import bat.ke.qq.com.zklock.service.OrderCodeGenerator;
import bat.ke.qq.com.zklock.service.OrderService;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  使用JDK中内置ReentrantLock锁
 */
public class OrderServiceWithLock implements OrderService {
    
    private Lock lock = new ReentrantLock();
    //    private OrderCodeGenerator orderCodeGenerator = new OrderCodeGenerator();
    //静态的 唯一订单编号生成器
    private volatile static OrderCodeGenerator orderCodeGenerator = new OrderCodeGenerator();
    
    @Override
    public void createOrder() {
        //获取订单号
        try {
            lock.lock();
            String orderCode = orderCodeGenerator.getOrderCode();
            System.out.println(Thread.currentThread().getName() + "==============>订单号为:" + orderCode);
        } finally {
            lock.unlock();
            
        }
    }
}