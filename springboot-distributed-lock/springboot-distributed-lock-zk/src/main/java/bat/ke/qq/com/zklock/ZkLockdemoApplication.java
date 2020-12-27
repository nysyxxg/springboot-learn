package bat.ke.qq.com.zklock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class ZkLockdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZkLockdemoApplication.class, args);
    }

}
