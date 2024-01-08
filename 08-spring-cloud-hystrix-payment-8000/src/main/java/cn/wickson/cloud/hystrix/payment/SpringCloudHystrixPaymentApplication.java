package cn.wickson.cloud.hystrix.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * hystrix 启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-08
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "cn.wickson.cloud")
public class SpringCloudHystrixPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudHystrixPaymentApplication.class, args);
    }

}
