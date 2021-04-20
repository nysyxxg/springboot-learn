package test;

import com.lnjecit.config.JavaConfig;
import com.lnjecit.config.MyBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyBeanMain {
    public static void main(String args[]) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        MyBean myBean = context.getBean(MyBean.class);
        myBean.main();
        context.close();
    }
}