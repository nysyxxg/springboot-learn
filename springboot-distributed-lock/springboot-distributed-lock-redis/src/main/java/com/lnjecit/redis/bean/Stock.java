package com.lnjecit.redis.bean;

public class Stock {
    //库存当前数量
    public static int count = 10;
    
    /**
     * 下订单，减少库存操作
     *
     * @return true 下单成功，false 下单失败
     */
    public static boolean reduceStock() {
        if (count <= 0) {
            return false;
        }
        try {
            Thread.sleep(20 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        count--;
        return true;
    }
}