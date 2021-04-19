package bat.ke.qq.com.zklock.service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class OrderCodeGenerator {
    private static int count = 0;
    
    //生成订单号
    public String getOrderCode() {
        try {
            Thread.sleep(1* 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date()) + "-" + ++count;
    }
}
