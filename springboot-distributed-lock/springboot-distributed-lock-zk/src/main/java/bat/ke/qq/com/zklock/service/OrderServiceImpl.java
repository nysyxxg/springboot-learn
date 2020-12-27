package bat.ke.qq.com.zklock.service;

public class OrderServiceImpl implements OrderService {

    private OrderCodeGenerator orderCodeGenerator = new OrderCodeGenerator();

    @Override
    public void createOrder() {
        //获取订单号
        String orderCode = orderCodeGenerator.getOrderCode();
        System.out.println(Thread.currentThread().getName() + "==============>订单号为:" + orderCode);
    }
}