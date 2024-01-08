package cn.wickson.cloud.hystrix.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 订单服务-启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-08
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "cn.wickson.cloud")
public class SpringCloudHystrixOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudHystrixOrderApplication.class, args);
    }

}
