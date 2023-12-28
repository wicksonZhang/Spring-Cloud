package cn.wickson.cloud.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 支付服务-微服务启用类
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@SpringBootApplication(scanBasePackages = {"cn.wickson.cloud"})
public class SpringCloudPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudPaymentApplication.class, args);
    }

}
